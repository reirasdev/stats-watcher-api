package com.reiras.statsapi.watcher;

public enum WatcherType {

	ON_CREATE("CREATE"), ON_MODIFY("MODIFY"), ON_DELETE("DELETE");

	private String type;

	private WatcherType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
