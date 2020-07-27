package com.reiras.statsapi.parser;

public abstract class ParserFactory {

	public static Parser getParser(String type) {

		if (type == null)
			return null;
		else if (ParserType.SALESMAN.getType().equalsIgnoreCase(type))
			return new SalesmanParser();
		else if (ParserType.CUSTOMER.getType().equalsIgnoreCase(type))
			return new CustomerParser();
		else if (ParserType.SALE.getType().equalsIgnoreCase(type))
			return new SaleParser();

		return null;
	}
}
