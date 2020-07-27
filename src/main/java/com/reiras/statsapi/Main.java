package com.reiras.statsapi;

import java.io.IOException;
import java.nio.file.Paths;

import com.reiras.statsapi.conf.Configuration;
import com.reiras.statsapi.processor.SalesProcessor;
import com.reiras.statsapi.watcher.FileWatcher;
import com.reiras.statsapi.watcher.WatcherType;

public class Main {

	public static void main(String[] args) throws IOException {
		
		FileWatcher.getInstance()
		.register(new SalesProcessor(),
				Paths.get(Configuration.IN_DIR),
				WatcherType.ON_MODIFY)
		.watch();
		
	}
}
