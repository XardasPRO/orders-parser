package com.unlimint.test.service.storage;

import com.unlimint.test.model.reader.LineRead;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ReadLinesQueue {
	private static final ConcurrentLinkedQueue<LineRead> storage = new ConcurrentLinkedQueue<>();

	public int getSize() {
		return storage.size();
	}

	public void add(LineRead lineRead) {
		storage.add(lineRead);
	}

	public LineRead poll() {
		return storage.poll();
	}
}
