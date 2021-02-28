package com.unlimint.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unlimint.test.model.order.Currency;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;

@JsonDeserialize(builder = OrderDto.OrderDtoBuilder.class)
@Value
@With
@Builder
public class OrderDto {
	@JsonProperty("orderId")
	Long orderId;
	@JsonProperty("amount")
	BigDecimal amount;
	@JsonProperty("currency")
	Currency currency;
	@JsonProperty("comment")
	String comment;
}
