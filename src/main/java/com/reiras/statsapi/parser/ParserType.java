package com.reiras.statsapi.parser;

public enum ParserType {

	SALESMAN("001"), CUSTOMER("002"), SALE("003");

	private String type;

	private ParserType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
