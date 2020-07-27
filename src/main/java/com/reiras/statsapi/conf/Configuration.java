package com.reiras.statsapi.conf;

import java.util.Optional;

public abstract class Configuration {

	public static final String IN_DIR;

	public static final String OUT_DIR;

	public static final String PROCESSED_DIR;

	static {
		String homePath = Optional.ofNullable(System.getenv("HOMEPATH")).orElse(".");

		IN_DIR = homePath + "\\data\\in";
		OUT_DIR = homePath + "\\data\\out";
		PROCESSED_DIR = homePath + "\\data\\processed";
	}
}
