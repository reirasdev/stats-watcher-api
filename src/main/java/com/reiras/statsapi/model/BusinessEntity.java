package com.reiras.statsapi.model;

import com.reiras.statsapi.statistic.StatisticStrategy;

public interface BusinessEntity {
	
	void summarizeStatistics(StatisticStrategy statisticStrategy);
	
}
