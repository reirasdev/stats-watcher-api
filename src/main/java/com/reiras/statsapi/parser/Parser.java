package com.reiras.statsapi.parser;

import com.reiras.statsapi.model.BusinessEntity;

public interface Parser {
	
	BusinessEntity parse(String[] args);
}
