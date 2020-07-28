package com.reiras.statsapi.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.reiras.statsapi.conf.Configuration;
import com.reiras.statsapi.model.BusinessEntity;
import com.reiras.statsapi.parser.Parser;
import com.reiras.statsapi.parser.ParserFactory;
import com.reiras.statsapi.statistic.SalesStatistic;

public class SalesProcessor implements Processor {

	public void process(Path path) {
		List<BusinessEntity> processedEntitiesList = new ArrayList<BusinessEntity>();
		List<String> processErrors = new ArrayList<String>();
		int status = this.parseBusinessEntities(path, processedEntitiesList, processErrors);

		if (status == 1) {	
			SalesStatistic salesStatistic = new SalesStatistic();
			processedEntitiesList.forEach(entity -> entity.summarizeStatistics(salesStatistic));
			
			this.writeOutputFile(Paths.get(Configuration.OUT_DIR).resolve(path.getFileName()), this.generateOutputContent(salesStatistic), processErrors);
			this.moveFile(path, Paths.get(Configuration.PROCESSED_DIR).resolve(path.getFileName()));

		} else if (status == -1) {
			System.err.printf("[ERROR] Not possible to process file %s. Retrying later \n", path);
			this.retry(path);
			
		} else if (status == -2) {
			this.retry(path);
		}

	}

	private int parseBusinessEntities(Path path, List<BusinessEntity> processedEntitiesList, List<String> errors) {
		int status = 1;
		
		if (!path.toFile().exists() || path.toFile().isDirectory())
			return status = 0;
		
		try (BufferedReader fileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"))) {
			Parser parser = null;
			Integer lineCount = 0;
			String line;

			while ((line = fileBufferedReader.readLine()) != null) {
				lineCount++;
				try {
					String[] lineToArray = line.split("ç");
					parser = ParserFactory.getParser(lineToArray[0]);

					if (parser == null) {						
						errors.add("[ERROR] Line: {lineCount}. Could not find a parser for this entity type: {line}"
								.replace("{lineCount}", lineCount.toString())
								.replace("{line}", line));
						continue;
					}

					processedEntitiesList.add(parser.parse(lineToArray));

				} catch (Exception e) {
					errors.add("[ERROR] Line {lineCount}. Error parsing: {line} Exception: {exception}"
							.replace("{lineCount}", lineCount.toString())
							.replace("{line}", line)
							.replace("{exception}", e.toString()));
			
				}

			}

		} catch (FileNotFoundException e) {
			//do nothing
			status = -2;
			
		} catch (IOException e) {			
			System.err.printf("[ERROR] Not possible to read file: %s \n", path);
			e.printStackTrace();
			status = -1;
		}

		return status;
	}

	private String generateOutputContent(SalesStatistic salesStatistic) {
		StringBuilder str = new StringBuilder("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman");
		str.append("\n");
		str.append(salesStatistic.getStatsCustomer());
		str.append(",");
		str.append(salesStatistic.getStatsSalesman());
		str.append(",");
		str.append(salesStatistic.getHighestSaleId());
		str.append(",");
		str.append(salesStatistic.getWorstSalesman());

		return str.toString();
	}

	private void writeOutputFile(Path path, String content, List<String> errors) {

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile()), "UTF-8"));
			bufferedWriter.write(content);
			
			if(!errors.isEmpty())
				bufferedWriter.write("\n\nERRORS: \n");
				
			for(String e : errors)
				bufferedWriter.write("- " + e + " \n");
			
			bufferedWriter.close();

		} catch (IOException e) {
			System.err.printf("[ERROR] Could not write output file to: %s \n", path);
			e.printStackTrace();
		}

	}

	private void moveFile(Path from, Path to) {
		try {
			Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);

		} catch (FileSystemException e) {
			//do nothing
			
		} catch (IOException e) {
			System.err.printf("[ERROR] Not possible to move file from: %s to: %s \n", from, to);
			e.printStackTrace();
		}

	}

	public void retry(Path path) {
		CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(10000);

				if (path.toFile().exists()) {
					System.out.printf("[INFO] Retrying file %s \n", path);
					this.process(path);
				}

			} catch (InterruptedException e) {
				System.err.printf("[ERROR] Not possible to reprocess file: %s \n", path);
				e.printStackTrace();
			}

		});
	}

}
