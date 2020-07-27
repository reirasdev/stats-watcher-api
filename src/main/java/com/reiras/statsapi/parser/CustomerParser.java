package com.reiras.statsapi.parser;

import com.reiras.statsapi.model.BusinessEntity;
import com.reiras.statsapi.model.Customer;

public class CustomerParser implements Parser {

	public BusinessEntity parse(String[] args) {

		return new Customer(args[1], args[2], args[3]);
	}

}
