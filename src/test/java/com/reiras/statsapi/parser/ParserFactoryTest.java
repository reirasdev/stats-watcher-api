package com.reiras.statsapi.parser;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class ParserFactoryTest {

	@Test
	public void getParser_givenNull_returnNull() {
		assertNull(ParserFactory.getParser(null));
	}
	
	@Test
	public void getParser_givenNonExistent_returnNull() {
		assertNull(ParserFactory.getParser("000"));
	}
	
	@Test
	public void getParser_given001_returnSalesmanParser() {
		assertTrue(ParserFactory.getParser("001") instanceof SalesmanParser);
	}
	
	@Test
	public void getParser_given002_returnCustomerParser() {
		assertTrue(ParserFactory.getParser("002") instanceof CustomerParser);
	}
	
	@Test
	public void getParser_given003_returnSalesmanParser() {
		assertTrue(ParserFactory.getParser("003") instanceof SaleParser);
	}
}
