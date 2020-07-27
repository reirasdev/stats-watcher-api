package com.reiras.statsapi.model;

import java.util.List;

import com.reiras.statsapi.statistic.StatisticStrategy;

public class Sale implements BusinessEntity {

	private long id;

	private List<SaleIten> itens;

	private String salesmanName;

	public Sale(long id, List<SaleIten> itens, String salesmanName) {
		super();
		this.id = id;
		this.itens = itens;
		this.salesmanName = salesmanName;
	}

	public long getId() {
		return id;
	}

	public List<SaleIten> getItens() {
		return itens;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public double getTotalPrice() {
		return itens.stream().mapToDouble(x -> x.getTotalPrice()).sum();
	}

	public void summarizeStatistics(StatisticStrategy statisticStrategy) {
		statisticStrategy.addStatsSales(this.id, this.salesmanName, this.getTotalPrice());
	}

}
