package com.reiras.statsapi.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class TestHelper {
	
	public static String createBaseFile() {
		StringBuilder str = new StringBuilder();
		str.append("001�1234567891234�Pedro�50000").append("\n");
		str.append("001�3245678865434�Paulo�40000.99").append("\n");
		str.append("002�2345675434544345�Jose da Silva�Rural").append("\n");
		str.append("002�2345675433444345�Eduardo Pereira�Rural").append("\n");
		str.append("003�10�[1-10-100,2-30-2.50,3-40-3.10]�Pedro").append("\n");
		str.append("003�08�[1-34-10,2-33-1.50,3-40-0.10]�Paulo").append("\n");

		return str.toString();
	}

	public static void writeFile(Path path, String content) {

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile()), "UTF-8"));
			bufferedWriter.write(content);
			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static List<String> getFileContent(Path path) throws IOException {
		List<String> lines = new ArrayList<String>();

		try (BufferedReader fileBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"))) {
			
			String line;
			while ((line = fileBufferedReader.readLine()) != null)
				lines.add(line);
			
		}

		return lines;
	}
	
}
