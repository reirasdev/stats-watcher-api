package com.reiras.statsapi.parser;

import java.util.ArrayList;
import java.util.List;

import com.reiras.statsapi.model.BusinessEntity;
import com.reiras.statsapi.model.Sale;
import com.reiras.statsapi.model.SaleIten;

public class SaleParser implements Parser {

	public BusinessEntity parse(String[] args) {
		String[] saleItenArray = args[2].subSequence(1, args[2].lastIndexOf("]")).toString().split(",");
		List<SaleIten> itensList = new ArrayList<SaleIten>();

		if (saleItenArray.length > 0 && !saleItenArray[0].isEmpty()) {
			String[] itenArray;
			for (String iten : saleItenArray) {
				itenArray = iten.split("-");
				itensList.add(new SaleIten(Long.parseLong(itenArray[0]), 
						Integer.parseInt(itenArray[1]),
						Double.parseDouble(itenArray[2])));
			}
		}

		return new Sale(Long.parseLong(args[1]), itensList, args[3]);
	}

	public static void main(String... args) {
		String line = "003Á11Á[]ÁPior Vendedor";
		SaleParser sp = new SaleParser();
		sp.parse(line.split("Á"));
	}

}
