package com.rongdu.common.model.jpa;

public class OrderFilter {

	public enum OrderType {
		DESC, ASC
	}

	public String name;
	public OrderType order;

	public OrderFilter(OrderType order, String name) {
		super();
		this.name = name;
		this.order = order;
	}

	public OrderFilter(String name) {
		super();
		this.name = name;
		this.order = OrderType.ASC;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderType getOrder() {
		return order;
	}

	public void setOrder(OrderType order) {
		this.order = order;
	}

}
