package com.reiras.statsapi.watcher;

import java.io.IOException;
import java.nio.file.Path;

import com.reiras.statsapi.processor.Processor;

public interface Watcher {

	Watcher register(Processor processor, Path path, WatcherType... events) throws IOException;

	void watch() throws IOException;
}
