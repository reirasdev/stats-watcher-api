package com.reiras.statsapi.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.model.Sale;

public class SaleParserTest {

	@Test
	public void parse_givenCorrectInput_returnSale() {
		String args = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
		Sale sale = (Sale) new SaleParser().parse(args.split("ç"));
		
		assertNotNull(sale);
		assertEquals(10, sale.getId());
		assertEquals("Pedro", sale.getSalesmanName());
		assertEquals(3, sale.getItens().size());
		assertEquals(1, sale.getItens().get(0).getId());
		
		assertEquals(10, sale.getItens().get(0).getQty());
		assertEquals(100, sale.getItens().get(0).getPrice());
		assertEquals(1000, sale.getItens().get(0).getTotalPrice());
		
		assertEquals(2, sale.getItens().get(1).getId());
		assertEquals(30, sale.getItens().get(1).getQty());
		assertEquals(2.50, sale.getItens().get(1).getPrice());
		assertEquals(75, sale.getItens().get(1).getTotalPrice());
		
		assertEquals(3, sale.getItens().get(2).getId());
		assertEquals(40, sale.getItens().get(2).getQty());
		assertEquals(3.10, sale.getItens().get(2).getPrice());
		assertEquals(124, sale.getItens().get(2).getTotalPrice());
			
	}
	
	@Test
	public void parse_givenIncorrectSizeArray_throwsArrayIndexOutOfBoundsException() {
		String args = "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]";
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenMissingIdFiledArray_throwsArrayIndexOutOfBoundsException() {
		String args = "003ç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(StringIndexOutOfBoundsException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectIdFormat_throwsNumberFormatException() {
		String args = "003ç10Xç[1-10-100,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(NumberFormatException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectSaleItenIdFormat_throwsNumberFormatException() {
		String args = "003ç10ç[1X-10-100,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(NumberFormatException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectSaleItenQtyFormat_throwsNumberFormatException() {
		String args = "003ç10ç[1-10X-100,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(NumberFormatException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectSaleItenPriceFormat_throwsNumberFormatException() {
		String args = "003ç10ç[1-10-100X,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(NumberFormatException.class, () -> new SaleParser().parse(args.split("ç")));
	}
	
	@Test
	public void parse_givenIncorrectSaleItenSizeArray_throwsArrayIndexOutOfBoundsException() {
		String args = "003ç10ç[1-10,2-30-2.50,3-40-3.10]çPedro";
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> new SaleParser().parse(args.split("ç")));
	}
}
