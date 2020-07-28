package com.reiras.statsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.conf.Configuration;
import com.reiras.statsapi.helper.TestHelper;
import com.reiras.statsapi.processor.SalesProcessor;
import com.reiras.statsapi.watcher.FileWatcher;
import com.reiras.statsapi.watcher.WatcherType;

@SuppressWarnings("rawtypes")
public class MainIntegrationTest {

	@Test
	public void main_givenAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException, InterruptedException {
		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				
				Main.main(null);
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "main_givenAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		TestHelper.writeFile(path, TestHelper.createBaseFile());

		//giving some time to watcher process the file...
		Thread.sleep(1000);
		
		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals(2, outLines.size());
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));

		parallelTask.cancel(true);
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}
	
	@Test
	public void main_givenNotAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException, InterruptedException {
		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				
				Main.main(null);
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "main_givenNotAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		StringBuilder fileContent = new StringBuilder();
		fileContent.append("001Á1234567891234ÁPedroÁ500X00").append("\n");
		fileContent.append("0Á2345675434544345ÁJose da SilvaÁRural").append("\n");
		fileContent.append("003Á08Á[1-10,2-33-1.50,3-40-0.10]ÁPaulo").append("\n");
		fileContent.append(TestHelper.createBaseFile());
		fileContent.append("001ÁPauloÁ40000.99").append("\n");
		fileContent.append("002Á2345675433444345ÁEduardo Pereira").append("\n");
		fileContent.append("003Á[1-10-100,2-30-2.50,3-40-3.10]ÁPedro").append("\n");
		
		TestHelper.writeFile(path, fileContent.toString());

		//giving some time to watcher process the file...
		Thread.sleep(1000);
		
		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));

		parallelTask.cancel(true);
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}
	
	@Test
	public void main_givenAllLinesCorrectMassiveFileCreation_shouldCreateCorrectOutput() throws IOException, InterruptedException {
		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				FileWatcher.getInstance()
						.register(new SalesProcessor(),
								Paths.get(Configuration.IN_DIR), 
								WatcherType.ON_CREATE)
						.watch();
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "main_givenAllLinesCorrectMassiveFileCreation";
		StringBuilder fileContent = new StringBuilder(TestHelper.createBaseFile());
		String bestSale = "003Á999dynaBestSaleÁ[1-10-100,2-20-100,3-30-200,4-1-dynaBestSale]ÁPaulo";
		String worstSale = "003Á08Á[1-1-dynaWorstSale]ÁworstVendor dynaWorstSale";
		String dynaSalesman = "001Á1234567891234dynaSalesmanÁSalesman dynaSalesmanÁ777dynaSalesman";
		String dynaCustomer = "002Á2345675434544345dynaCustomerÁCustomer dynaCustomerÁArea dynaCustomer";
		Path path;
		
		Integer worstPrice = 101;
		for (Integer i = 0; i < 100; i++) {
			worstPrice--;
			path = Paths.get(Configuration.IN_DIR).resolve(fileName + i + ".txt");
			TestHelper.writeFile(path, fileContent
					.append(bestSale.replace("dynaBestSale", i.toString())).append("\n")
					.append(worstSale.replace("dynaWorstSale", worstPrice.toString())).append("\n")
					.append(dynaSalesman.replace("dynaSalesman", i.toString())).append("\n")
					.append(dynaCustomer.replace("dynaCustomer", i.toString())).append("\n")
					.toString());
		}

		//giving some time to watcher process the file...
		Thread.sleep(3000);
		
		for (int i = 0; i < 100; i++) {
			path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());
		}		

		worstPrice = 101;
		for (int i = 0; i < 100; i++) {
			worstPrice--;
			path = Paths.get(Configuration.OUT_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());

			List<String> outLines = TestHelper.getFileContent(path);
			assertEquals(2, outLines.size());
			assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
			assertEquals((3 + i) + "," + (3 + i) + ",999" + i + ",worstVendor " + worstPrice.toString(), outLines.get(1));
		}
		
		parallelTask.cancel(true);
		Files.walk(Paths.get(Configuration.PROCESSED_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
		Files.walk(Paths.get(Configuration.OUT_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
	
	}
	
	@Test
	public void main_givenNotAllLinesCorrectMassiveFileCreation_shouldCreateCorrectOutput() throws IOException, InterruptedException {

		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				FileWatcher.getInstance()
						.register(new SalesProcessor(),
								Paths.get(Configuration.IN_DIR), 
								WatcherType.ON_CREATE)
						.watch();
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "main_givenNotAllLinesCorrectMassiveFileCreation";
		StringBuilder fileContent = new StringBuilder(TestHelper.createBaseFile());
		String bestSale = "003Á999dynaBestSaleÁ[1-10-100,2-20-100,3-30-200,4-1-dynaBestSale]ÁPaulo";
		String worstSale = "003Á08Á[1-1-dynaWorstSale]ÁworstVendor dynaWorstSale";
		String dynaSalesman = "001Á1234567891234dynaSalesmanÁSalesman dynaSalesmanÁ777dynaSalesman";
		String dynaCustomer = "002Á2345675434544345dynaCustomerÁCustomer dynaCustomerÁArea dynaCustomer";
		
		Path path;
		
		Integer worstPrice = 101;
		for (Integer i = 0; i < 100; i++) {
			worstPrice--;
			path = Paths.get(Configuration.IN_DIR).resolve(fileName + i + ".txt");
			TestHelper.writeFile(path, fileContent
					.append(bestSale.replace("dynaBestSale", i.toString())).append("\n")
					.append(worstSale.replace("dynaWorstSale", worstPrice.toString())).append("\n")
					.append(dynaSalesman.replace("dynaSalesman", i.toString())).append("\n")
					.append(dynaCustomer.replace("dynaCustomer", i.toString())).append("\n")
					.append("001Á1234567891234ÁPedroÁ500X00").append("\n")
					.append("0Á2345675434544345ÁJose da SilvaÁRural").append("\n")
					.append("003Á08Á[1-10,2-33-1.50,3-40-0.10]ÁPaulo").append("\n")
					.toString());
		}

		//giving some time to watcher process the file...
		Thread.sleep(5000);
		
		for (int i = 0; i < 100; i++) {
			path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());
		}		

		worstPrice = 101;
		for (int i = 0; i < 100; i++) {
			worstPrice--;
			path = Paths.get(Configuration.OUT_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());

			List<String> outLines = TestHelper.getFileContent(path);
			assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
			assertEquals((3 + i) + "," + (3 + i) + ",999" + i + ",worstVendor " + worstPrice.toString(), outLines.get(1));
		}
		
		parallelTask.cancel(true);
		Files.walk(Paths.get(Configuration.PROCESSED_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
		Files.walk(Paths.get(Configuration.OUT_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
	
	}
	
}
