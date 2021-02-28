package com.unlimint.test.service.printer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unlimint.test.model.order.Order;
import com.unlimint.test.service.converter.ConvertIsDone;
import com.unlimint.test.service.storage.OrdersQueue;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class Printer {
	private final ConvertIsDone convertIsDone;
	private final OrdersQueue ordersQueue;

	public Printer(ConvertIsDone convertIsDone, OrdersQueue ordersQueue) {
		this.convertIsDone = convertIsDone;
		this.ordersQueue = ordersQueue;
	}

	public void start() {
		Executors.newSingleThreadExecutor().submit(() -> {
			ObjectMapper objectMapper = new ObjectMapper();
			Order order = ordersQueue.poll();
			while (order != null || !convertIsDone.isDone()) {
				if (order != null) {
					try {
						System.out.println(objectMapper.writeValueAsString(order));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
				order = ordersQueue.poll();
			}
			System.exit(0);
		});
	}
}
