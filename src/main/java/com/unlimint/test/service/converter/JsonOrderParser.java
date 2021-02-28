package com.unlimint.test.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unlimint.test.dto.OrderDto;
import com.unlimint.test.model.order.Order;
import org.springframework.stereotype.Service;

@Service
public class JsonOrderParser implements OrderParser{
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Order parse(String line) {
		return parseJsonString(line);
	}

	private Order parseJsonString(String line) {
		try {
			return fromTransport(objectMapper.readValue(line, OrderDto.class)).withResult("OK");
		} catch (JsonProcessingException e) {
		return Order.builder()
				.result("Parsing error: " + e.getMessage())
				.build();
		}
	}

	Order fromTransport(OrderDto dto) {
		return Order.builder()
				.id(dto.getOrderId())
				.amount(dto.getAmount())
				.currency(dto.getCurrency())
				.comment(dto.getComment())
				.build();
	}
}
