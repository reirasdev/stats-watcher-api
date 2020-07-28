package com.reiras.statsapi.processor;

import java.nio.file.Path;

public interface Processor {

	void process(Path path);
	
	void retry(Path path);
	
}
