package com.reiras.statsapi.watcher;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import com.reiras.statsapi.conf.Configuration;
import com.reiras.statsapi.helper.TestHelper;
import com.reiras.statsapi.processor.SalesProcessor;

@SuppressWarnings("rawtypes")
public class FileWatcherTest {

	@Test
	public void watch_givenCreateEvent_shouldMoveFile() throws IOException, InterruptedException {

		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				FileWatcher.getInstance()
						.register(new SalesProcessor(),
								Paths.get(Configuration.IN_DIR), 
								WatcherType.ON_CREATE)
						.watch();
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "watch_givenCreateEvent.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		TestHelper.writeFile(path, "");

		//giving some time to watcher process the file...
		Thread.sleep(1000);
		
		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		parallelTask.cancel(true);
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
	
	}
	
	@Test
	public void watch_givenCreateEventMassiveFileCreaction_shouldMoveFiles() throws IOException, InterruptedException {

		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				FileWatcher.getInstance()
						.register(new SalesProcessor(),
								Paths.get(Configuration.IN_DIR), 
								WatcherType.ON_CREATE)
						.watch();
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		String fileName = "watch_givenCreateEventMassiveFileCreaction";
		Path path;
		
		for (int i = 0; i < 100; i++) {
			path = Paths.get(Configuration.IN_DIR).resolve(fileName + i + ".txt");
			TestHelper.writeFile(path, "");
		}

		//giving some time to watcher process the file...
		Thread.sleep(3000);
		
		for (int i = 0; i < 100; i++) {
			path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());
		}

		for (int i = 0; i < 100; i++) {
			path = Paths.get(Configuration.OUT_DIR).resolve(fileName + i + ".txt");
			assertTrue(path.toFile().exists());
		}
		
		parallelTask.cancel(true);
		Files.walk(Paths.get(Configuration.PROCESSED_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
		Files.walk(Paths.get(Configuration.OUT_DIR)).map(x -> x.toFile()).forEach(x -> x.delete());
	
	}
	
	@Test
	public void watch_givenModifyEvent_shouldMoveFile() throws IOException, InterruptedException {
		String fileName = "watch_givenModifyEvent.txt";
		Path path = Paths.get(Configuration.IN_DIR).resolve(fileName);
		TestHelper.writeFile(path, "");
		
		CompletableFuture parallelTask = CompletableFuture.runAsync(() -> {
			try {
				FileWatcher.getInstance()
						.register(new SalesProcessor(),
								Paths.get(Configuration.IN_DIR), 
								WatcherType.ON_MODIFY)
						.watch();
				
			} catch (IOException e) {
				System.err.printf("[ERROR] Please, check if %s directory is created \n", Configuration.IN_DIR);
				e.printStackTrace();
			}
		});
		
		//giving some time to watcher start monitoring the path
		Thread.sleep(1000);
		
		//touching the file
		path.toFile().setLastModified(System.currentTimeMillis());
		
		//giving some time to watcher process the file...
		Thread.sleep(1000);
		
		path = Paths.get(Configuration.PROCESSED_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		path = Paths.get(Configuration.OUT_DIR).resolve(fileName);
		assertTrue(path.toFile().exists());

		parallelTask.cancel(true);
		Paths.get(Configuration.PROCESSED_DIR).resolve(fileName).toFile().delete();
		Paths.get(Configuration.OUT_DIR).resolve(fileName).toFile().delete();
		
	}
	
	@Test
	public void register_givenValidPath_returnFileWatcher() throws IOException {
		Watcher fileWatcher = FileWatcher.getInstance().register(new SalesProcessor(), Paths.get(Configuration.IN_DIR),
				WatcherType.ON_MODIFY);

		assertNotNull(fileWatcher);
		assertTrue(fileWatcher instanceof FileWatcher);
	}

	@Test
	public void register_givenInvalidPath_throwsIOException() throws IOException {
		assertThrows(IOException.class, () -> FileWatcher.getInstance().register(new SalesProcessor(),
				Paths.get("register_givenInvalidPath_throws"), WatcherType.ON_MODIFY));
	}
	
}
