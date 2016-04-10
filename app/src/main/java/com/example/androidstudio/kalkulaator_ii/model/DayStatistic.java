package com.example.androidstudio.kalkulaator_ii.model;

import java.sql.Date;

public class DayStatistic implements IEntity {
	private long id;
	private long operandId;
	private Operand operand;
	private Date created;
	private int count;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOperandId() {
		return operandId;
	}

	public void setOperandId(long operandId) {
		this.operandId = operandId;
	}

	public Operand getOperand() {
		return operand;
	}

	public void setOperand(Operand operand) {
		this.operand = operand;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}