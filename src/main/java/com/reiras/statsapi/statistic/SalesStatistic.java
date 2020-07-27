package com.reiras.statsapi.statistic;

import java.util.HashMap;
import java.util.Map;

public class SalesStatistic implements StatisticStrategy {
	private int statsSalesman = 0;

	private int statsCustomer = 0;

	private double highestSalePrice = 0.0;

	private long highestSaleId = 0;

	private Map<String, Double> salesmanSaleMap = new HashMap<String, Double>();

	@Override
	public void addStatsSalesmans() {
		this.statsSalesman++;

	}

	@Override
	public void addStatsCustomers() {
		this.statsCustomer++;

	}

	@Override
	public void addStatsSales(long saleId, String salesmanName, double saleTotalPrice) {
		salesmanSaleMap.compute(salesmanName, (key, val) -> val == null ? saleTotalPrice : val + saleTotalPrice);

		if (saleTotalPrice > highestSalePrice) {
			highestSalePrice = saleTotalPrice;
			highestSaleId = saleId;
		}
	}

	public int getStatsSalesman() {
		return this.statsSalesman;
	}

	public int getStatsCustomer() {
		return this.statsCustomer;
	}

	public long getHighestSaleId() {
		return highestSaleId;
	}

	public String getWorstSalesman() {
		if (salesmanSaleMap.isEmpty())
			return "";

		return salesmanSaleMap.entrySet()
				.stream()
				.min((d1, d2) -> Double.compare(d1.getValue(), d2.getValue()))
				.get()
				.getKey();
	}

}
