package com.reiras.statsapi.model;

public class SaleIten {

	private long id;

	private int qty;

	private double price;

	public SaleIten(long id, int qty, double price) {
		this.id = id;
		this.qty = qty;
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public int getQty() {
		return qty;
	}

	public double getPrice() {
		return price;
	}

	public double getTotalPrice() {
		return price * qty;
	}
}
