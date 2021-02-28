package com.unlimint.test;

import com.unlimint.test.service.converter.Converter;
import com.unlimint.test.service.printer.Printer;
import com.unlimint.test.service.reader.FileReader;
import com.unlimint.test.service.reader.FileReaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TestApplication implements ApplicationRunner {
	private final FileReader fileReader;
	private final Converter converter;
	private final Printer printer;

	@Autowired
	public TestApplication(FileReaderImpl fileReader, Converter converter, Printer printer) {
		this.fileReader = fileReader;
		this.converter = converter;
		this.printer = printer;
	}

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) {
		String[] sourceArgs = args.getSourceArgs();
		for (String sourceArg : sourceArgs) {
			fileReader.read(sourceArg);
		}
		converter.start();
		printer.start();
	}
}
