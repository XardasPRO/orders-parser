package com.unlimint.test.service.converter;

import com.unlimint.test.model.order.Order;

public interface OrderParser {
	Order parse(String line);
}
