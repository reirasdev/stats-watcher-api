package com.reiras.statsapi.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.conf.Configuration;
import com.reiras.statsapi.helper.TestHelper;

public class SalesProcessorTest {

	@Test
	public void process_givenAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException {
		SalesProcessor salesProcessor = new SalesProcessor();

		String fileName = "process_givenAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		TestHelper.writeFile(path, TestHelper.createBaseFile());

		salesProcessor.process(path);

		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals(2, outLines.size());
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));
		
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}
	
	@Test
	public void process_givenNotAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException {
		SalesProcessor salesProcessor = new SalesProcessor();

		String fileName = "process_givenNotAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		StringBuilder fileContent = new StringBuilder();
		fileContent.append("001�1234567891234�Pedro�500X00").append("\n");
		fileContent.append("0�2345675434544345�Jose da Silva�Rural").append("\n");
		fileContent.append("003�08�[1-10,2-33-1.50,3-40-0.10]�Paulo").append("\n");
		fileContent.append(TestHelper.createBaseFile());
		fileContent.append("001�Paulo�40000.99").append("\n");
		fileContent.append("002�2345675433444345�Eduardo Pereira").append("\n");
		fileContent.append("003�[1-10-100,2-30-2.50,3-40-3.10]�Pedro").append("\n");

		TestHelper.writeFile(path, fileContent.toString());
		salesProcessor.process(path);

		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));
		
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}
	
	@Test
	public void retry_givenAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException, InterruptedException {
		SalesProcessor salesProcessor = new SalesProcessor();

		String fileName = "retry_givenAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		TestHelper.writeFile(path, TestHelper.createBaseFile());

		salesProcessor.retry(path);
		Thread.sleep(11000);

		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals(2, outLines.size());
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));
		
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}
	
	@Test
	public void retry_givenNotAllLinesCorrectFile_shouldCreateCorrectOutput() throws IOException, InterruptedException {
		SalesProcessor salesProcessor = new SalesProcessor();

		String fileName = "retry_givenNotAllLinesCorrectFile.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		StringBuilder fileContent = new StringBuilder(TestHelper.createBaseFile());
		fileContent.append("001�1234567891234�Pedro�500X00").append("\n");
		fileContent.append("0�2345675434544345�Jose da Silva�Rural").append("\n");
		fileContent.append("003�08�[1-10,2-33-1.50,3-40-0.10]�Paulo").append("\n");

		TestHelper.writeFile(path, fileContent.toString());
		salesProcessor.retry(path);
		Thread.sleep(11000);

		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());
		
		List<String> outLines = TestHelper.getFileContent(path);
		assertEquals("customerQuantity,salesmanQuantity,highestSaleId,worstSalesman", outLines.get(0));
		assertEquals("2,2,10,Paulo", outLines.get(1));
		
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	}

}
