package com.unlimint.test.service.reader;

import com.unlimint.test.model.reader.FileType;
import com.unlimint.test.model.reader.LineRead;
import com.unlimint.test.service.storage.ReadLinesQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FileReaderImpl implements FileReader {
	private final ReadLinesQueue readLinesQueue;
	private final ReadIsDone readIsDone;

	@Autowired
	public FileReaderImpl(ReadLinesQueue readLinesQueue, ReadIsDone readIsDone) {
		this.readLinesQueue = readLinesQueue;
		this.readIsDone = readIsDone;
	}

	@Override
	public void read(String file) {
		FileType fileType = recognizeFileType(file);
		if (fileType == null) {
			System.out.println("Unsupported file type: " + file);
			return;
		}
		readIsDone.add(file);
		new Thread(() -> {
			try {
				BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
				readLines(reader, file, fileType);
			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException: " + e.getMessage());
			} finally {
				readIsDone.done(file);
			}
		}).start();
	}

	private void readLines(BufferedReader reader, String fileName, FileType fileType) {
		long lineCount = 0;
		try {
			String readLine = reader.readLine();
			while (readLine != null) {
				LineRead lineRead = LineRead.builder()
						.filename(fileName)
						.lineNumber(lineCount)
						.line(readLine)
						.fileType(fileType)
						.build();
				putToStorage(lineRead);
				readLine = reader.readLine();
				lineCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void putToStorage(LineRead lineRead) {
		while (readLinesQueue.getSize() > 20000) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		readLinesQueue.add(lineRead);
	}

	private FileType recognizeFileType(String fileName) {
		if (fileName.toLowerCase().endsWith(".csv")) {
			return FileType.CSV;
		}
		if (fileName.toLowerCase().endsWith(".json")) {
			return FileType.JSON;
		}
		return null;
	}
}
