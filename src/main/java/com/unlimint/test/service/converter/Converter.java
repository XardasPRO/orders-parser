package com.unlimint.test.service.converter;

import com.unlimint.test.model.order.Order;
import com.unlimint.test.model.reader.LineRead;
import com.unlimint.test.service.reader.ReadIsDone;
import com.unlimint.test.service.storage.OrdersQueue;
import com.unlimint.test.service.storage.ReadLinesQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class Converter {
	private final CSVOrderParser csvOrderParser;
	private final JsonOrderParser jsonOrderParser;
	private final ReadLinesQueue readLinesQueue;
	private final ReadIsDone readIsDone;
	private final OrdersQueue ordersQueue;
	private final ConvertIsDone convertIsDone;


	@Autowired
	public Converter(CSVOrderParser csvOrderParser, JsonOrderParser jsonOrderParser, ReadLinesQueue readLinesQueue, ReadIsDone readIsDone, OrdersQueue ordersQueue, ConvertIsDone convertIsDone) {
		this.csvOrderParser = csvOrderParser;
		this.jsonOrderParser = jsonOrderParser;
		this.readLinesQueue = readLinesQueue;
		this.readIsDone = readIsDone;
		this.ordersQueue = ordersQueue;
		this.convertIsDone = convertIsDone;
	}

	public void start() {
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
			executorService.submit(createTask());
		}
	}

	private Order parse(OrderParser orderParser, LineRead lineRead) {
		return orderParser.parse(lineRead.getLine())
				.withLine(lineRead.getLineNumber())
				.withFilename(lineRead.getFilename());
	}

	private Runnable createTask() {
		return () -> {
			String id = UUID.randomUUID().toString();
			convertIsDone.add(id);
			LineRead lineRead = readLinesQueue.poll();
			while (lineRead != null || !readIsDone.isDone()) {
				if (lineRead != null) {
					Order order = Order.builder()
							.filename(lineRead.getFilename())
							.line(lineRead.getLineNumber())
							.result("Unknown parse exception")
							.build();
					switch (lineRead.getFileType()) {
						case CSV:
							order = parse(csvOrderParser, lineRead);
							break;
						case JSON:
							order = parse(jsonOrderParser, lineRead);
							break;
					}
					while (ordersQueue.getSize() > 20000) {
						sleep();
					}
					ordersQueue.add(order);
				} else {
					sleep();
				}
				lineRead = readLinesQueue.poll();
			}
			convertIsDone.done(id);
		};
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
