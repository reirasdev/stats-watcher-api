package com.reiras.statsapi.statistic;

public interface StatisticStrategy {

	void addStatsSalesmans();

	void addStatsCustomers();

	void addStatsSales(long saleId, String salesmanName, double saleTotalPrice);

}
