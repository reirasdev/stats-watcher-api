package com.reiras.statsapi.parser;

import com.reiras.statsapi.model.BusinessEntity;
import com.reiras.statsapi.model.Salesman;

public class SalesmanParser implements Parser {

	public BusinessEntity parse(String[] args) {

		return new Salesman(args[1], args[2], Double.parseDouble(args[3]));
	}

}
