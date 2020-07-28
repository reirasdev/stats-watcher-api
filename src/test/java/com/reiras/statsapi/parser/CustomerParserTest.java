package com.reiras.statsapi.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.model.Customer;

public class CustomerParserTest {

	@Test
	public void parse_givenCorrectInput_returnCustomer() {
		String args = "002�2345675434544345�Jose da Silva�Rural";
		Customer customer = (Customer) new CustomerParser().parse(args.split("�"));
		
		assertNotNull(customer);
		assertEquals("2345675434544345", customer.getFiscalId());
		assertEquals("Jose da Silva", customer.getName());
		assertEquals("Rural", customer.getBusinessArea());
	}
	
	@Test
	public void parse_givenIncorrectSizeArray_throwsArrayIndexOutOfBoundsException() {
		String args = "002�Jose da Silva�Rural";
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> new CustomerParser().parse(args.split("�")));
	}
	
}
