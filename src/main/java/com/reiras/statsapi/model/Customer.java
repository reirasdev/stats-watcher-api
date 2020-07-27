package com.reiras.statsapi.model;

import com.reiras.statsapi.statistic.StatisticStrategy;

public class Customer extends AbstractPerson implements BusinessEntity {

	private String businessArea;

	public Customer(String fiscalId, String name, String businessArea) {
		super(fiscalId, name);
		this.businessArea = businessArea;
	}

	public String getBusinessArea() {
		return businessArea;
	}

	public void summarizeStatistics(StatisticStrategy statisticStrategy) {
		statisticStrategy.addStatsCustomers();
	}

}
