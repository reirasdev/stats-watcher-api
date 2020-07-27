package com.reiras.statsapi.processor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
		int status = this.parseBusinessEntities(path, processedEntitiesList);

		if (status == 1) {	
			SalesStatistic salesStatistic = new SalesStatistic();
			processedEntitiesList.forEach(entity -> entity.summarizeStatistics(salesStatistic));
			
			this.writeOutputFile(Paths.get(Configuration.OUT_DIR).resolve(path.getFileName()), this.generateOutputContent(salesStatistic));
			this.moveFile(path, Paths.get(Configuration.PROCESSED_DIR).resolve(path.getFileName()));

		} else if (status == -1) {
			System.err.printf("[ERROR] Not possible to process file %s. Retrying later \n", path);
			this.asyncRetry(path);
			
		} else if (status == -2) {
			this.asyncRetry(path);
		}

	}

	private int parseBusinessEntities(Path path, List<BusinessEntity> processedEntitiesList) {
		int status = 1;
		
		if (!path.toFile().exists() || path.toFile().isDirectory())
			return status = 0;
		
		try (BufferedReader fileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"))) {
			Parser parser = null;
			int lineCount = 0;
			String line;

			while ((line = fileBufferedReader.readLine()) != null) {
				lineCount++;
				try {
					String[] lineToArray = line.split("ç");
					parser = ParserFactory.getParser(lineToArray[0]);

					if (parser == null) {
						System.err.printf("[ERROR] Line: %s. File %s. Could not find a parser for this entity type: %s \n", lineCount, path, line);
						continue;
					}

					processedEntitiesList.add(parser.parse(lineToArray));

				} catch (Exception e) {
					System.err.printf("[ERROR] Line %s. File %s. Error parsing. Invalid line is: %s \n", lineCount, path, line);
					e.printStackTrace();
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

	private void writeOutputFile(Path path, String content) {

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
			bufferedWriter.write(content);
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

	private void asyncRetry(Path path) {
		CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(10000);

				if (path.toFile().exists()) {
					System.out.printf("[INFO] Retrying file %s \n", path);
					path.toFile().setLastModified(System.currentTimeMillis());
				}

			} catch (InterruptedException e) {
				System.err.printf("[ERROR] Not possible to reprocess file: %s \n", path);
				e.printStackTrace();
			}

		});
	}

}
