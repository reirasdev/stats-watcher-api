package com.reiras.statsapi.model;

import com.reiras.statsapi.statistic.StatisticStrategy;

public class Salesman extends AbstractPerson implements BusinessEntity {

	private double salary;

	public Salesman(String fiscalId, String name, double salary) {
		super(fiscalId, name);
		this.salary = salary;
	}

	public double getSalary() {
		return salary;
	}

	public void summarizeStatistics(StatisticStrategy statisticStrategy) {
		statisticStrategy.addStatsSalesmans();
	}

}
