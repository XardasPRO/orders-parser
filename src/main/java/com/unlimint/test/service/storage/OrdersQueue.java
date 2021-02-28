package com.unlimint.test.service.storage;

import com.unlimint.test.model.order.Order;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class OrdersQueue {
	private static final ConcurrentLinkedQueue<Order> storage = new ConcurrentLinkedQueue<>();

	public int getSize() {
		return storage.size();
	}

	public void add(Order lineRead) {
		storage.add(lineRead);
	}

	public Order poll() {
		return storage.poll();
	}
}
