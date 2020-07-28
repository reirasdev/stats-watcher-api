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
		fileContent.append("001Á1234567891234ÁPedroÁ500X00").append("\n");
		fileContent.append("0Á2345675434544345ÁJose da SilvaÁRural").append("\n");
		fileContent.append("003Á08Á[1-10,2-33-1.50,3-40-0.10]ÁPaulo").append("\n");
		fileContent.append(TestHelper.createBaseFile());
		fileContent.append("001ÁPauloÁ40000.99").append("\n");
		fileContent.append("002Á2345675433444345ÁEduardo Pereira").append("\n");
		fileContent.append("003Á[1-10-100,2-30-2.50,3-40-3.10]ÁPedro").append("\n");

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
		fileContent.append("001Á1234567891234ÁPedroÁ500X00").append("\n");
		fileContent.append("0Á2345675434544345ÁJose da SilvaÁRural").append("\n");
		fileContent.append("003Á08Á[1-10,2-33-1.50,3-40-0.10]ÁPaulo").append("\n");

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
