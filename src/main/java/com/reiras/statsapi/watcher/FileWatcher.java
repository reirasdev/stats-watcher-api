package com.reiras.statsapi.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import com.reiras.statsapi.processor.Processor;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FileWatcher implements Watcher {

	private final WatchService fileWatcher;

	private final Map<Path, Processor> pathProcessorMap = new HashMap<Path, Processor>();

	private static FileWatcher instance;

	public static FileWatcher getInstance() throws IOException {
		if (instance == null) {

			synchronized (FileWatcher.class) {
				if (instance == null)
					instance = new FileWatcher(FileSystems.getDefault().newWatchService());

			}

		}

		return instance;
	}

	private FileWatcher(WatchService watcher) {
		this.fileWatcher = watcher;
	}

	public synchronized Watcher register(Processor processor, Path path, WatcherType... events) throws IOException {
		if (path == null)
			throw new IllegalArgumentException("Path cannot be null");
		if (events == null)
			throw new IllegalArgumentException("Events cannot be null, at least one event is necessary");
		
		WatchEvent.Kind[] watchEventArray;

		if (events.length > 0) {
			watchEventArray = new WatchEvent.Kind[events.length];
			for (int i = 0; i < events.length; i++) {
				if (events[i].equals(WatcherType.ON_CREATE))
					watchEventArray[i] = StandardWatchEventKinds.ENTRY_CREATE;
				else if (events[i].equals(WatcherType.ON_MODIFY))
					watchEventArray[i] = StandardWatchEventKinds.ENTRY_MODIFY;
			}
			
		} else {
			watchEventArray = new WatchEvent.Kind[3];
			watchEventArray[0] = StandardWatchEventKinds.ENTRY_CREATE;
			watchEventArray[1] = StandardWatchEventKinds.ENTRY_MODIFY;
		}

		path.register(this.fileWatcher, watchEventArray);
		pathProcessorMap.put(path, processor);
		
		return this;
	}

	public void watch() {
		WatchKey watchKey;

		while (true) {
			System.out.println("Waiting for new files...");
			
			try {
				watchKey = fileWatcher.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}

			Path pathWatchable = (Path) watchKey.watchable();
			if (!pathProcessorMap.containsKey(pathWatchable)) {
				System.out.printf("[WARNING] There is no processor for this path: %s. Skipping it \n", pathWatchable);
				continue;
			}

			Path fileTouched;
			for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {

				fileTouched = ((WatchEvent<Path>) watchEvent).context();
				if (fileTouched.toFile().isDirectory()) {
					System.err.printf("[ERROR] A directory cannot be proccessed: %s. Skipping it \n", pathWatchable.resolve(fileTouched));
					continue;
				}

				pathProcessorMap.get(pathWatchable).process(pathWatchable.resolve(fileTouched));
			}

			watchKey.reset();
		}
	}

}
