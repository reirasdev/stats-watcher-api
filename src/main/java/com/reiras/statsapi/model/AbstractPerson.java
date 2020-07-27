package com.reiras.statsapi.model;

public abstract class AbstractPerson {

	private String fiscalId;

	private String name;

	public AbstractPerson(String fiscalId, String name) {
		super();
		this.fiscalId = fiscalId;
		this.name = name;
	}

	public String getFiscalId() {
		return fiscalId;
	}

	public String getName() {
		return name;
	}
}
