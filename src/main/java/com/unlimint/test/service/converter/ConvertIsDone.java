package com.unlimint.test.service.converter;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConvertIsDone {
	private static final ConcurrentHashMap<String, Boolean> convertStatus = new ConcurrentHashMap<>();

	public boolean isDone() {
		if (convertStatus.values().isEmpty())
			return false;
		for (Boolean value : convertStatus.values()) {
			if (!value) {
				return false;
			}
		}
		return true;
	}

	public void add(String id) {
		convertStatus.put(id, false);
	}

	public void done(String id) {
		convertStatus.put(id, true);
	}
}
