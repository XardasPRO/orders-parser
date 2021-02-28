package com.unlimint.test.service.reader;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReadIsDone {
	private static final ConcurrentHashMap<String, Boolean> filesStatus = new ConcurrentHashMap<>();

	public boolean isDone() {
		for (Boolean value : filesStatus.values()) {
			if (!value) {
				return false;
			}
		}
		return true;
	}

	public void add(String filename) {
		filesStatus.put(filename, false);
	}

	public void done(String filename) {
		filesStatus.put(filename, true);
	}
}
