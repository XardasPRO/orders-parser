package com.unlimint.test.model.order;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;


@Value
@With
@Builder
public class Order {
	Long id;
	BigDecimal amount;
	Currency currency;
	String comment;
	String filename;
	Long line;
	String result;
}
