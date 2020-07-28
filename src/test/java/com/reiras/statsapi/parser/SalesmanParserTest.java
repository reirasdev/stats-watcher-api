package com.reiras.statsapi.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.model.Salesman;

public class SalesmanParserTest {

	@Test
	public void parse_givenCorrectInput_returnSalesman() {
		String args = "001ç3245678865434çPauloç40000.99";
		Salesman salesman = (Salesman) new SalesmanParser().parse(args.split("ç"));
		
		assertNotNull(salesman);
		assertEquals("3245678865434", salesman.getFiscalId());
		assertEquals("Paulo", salesman.getName());
		assertEquals(40000.99, salesman.getSalary());
	}
	
	@Test
	public void parse_givenIncorrectSizeArray_throwsArrayIndexOutOfBoundsException() {
		String args = "001çPauloç40000.99";		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> new SalesmanParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectSalaryFormat_throwsNumberFormatException() {
		String args = "001ç3245678865434çPauloç40X00.99";
		assertThrows(NumberFormatException.class, () -> new SalesmanParser().parse(args.split("ç")));
	}
}
