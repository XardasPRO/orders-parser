package com.unlimint.test.service.converter;

import com.unlimint.test.model.order.Currency;
import com.unlimint.test.model.order.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CSVOrderParser implements OrderParser {
	@Override
	public Order parse(String line) {
		return parseCsvString(line);
	}

	private Order parseCsvString(String line) {
		String[] values = line.split(",");
		if (values.length != 4) {
			return Order.builder().result("Invalid argument count").build();
		}

		String result = "";
		Long id = null;
		BigDecimal amount = null;
		Currency currency = null;

		try {
			id = Long.valueOf(values[0]);
		} catch (NumberFormatException e) {
			result += "Invalid id format: " + values[0] + ". ";
		}

		try {
			amount = new BigDecimal(values[1]);
		} catch (NumberFormatException e) {
			result += "Invalid amount format: " + values[1] + ". ";
		}

		try {
			currency = Currency.valueOf(values[2]);
		} catch (IllegalArgumentException e) {
			result += "Invalid currency format: " + values[2] + ". ";
		}

		return Order.builder()
				.id(id)
				.amount(amount)
				.currency(currency)
				.comment(values[3])
				.result(result.isEmpty() ? "OK" : result)
				.build();


	}
}
