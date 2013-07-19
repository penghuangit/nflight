package com.abreqadhabra.nflight.samples;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {

		Path filePath = Paths.get("C:", "temp", "test.txt");
		System.out.println("filePath : " + filePath);
		filePath = filePath.resolve("text.properties");
		System.out.println("filePath : " + filePath);

	}
}
