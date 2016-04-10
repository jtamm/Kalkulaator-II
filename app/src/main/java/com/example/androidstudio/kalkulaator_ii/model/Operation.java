package com.example.androidstudio.kalkulaator_ii.model;

import java.sql.Date;

public class Operation implements IEntity {
	private long id;
	private long expressionId;
	private Expression expression;
	private long operandId;
	private Operand operand;
	private float num1;
	private float num2;
	private float result;
	private long resultTypeId;
	private DoubleType resultType;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExpressionId() {
		return expressionId;
	}
	public void setExpressionId(long expressionId) {
		this.expressionId = expressionId;
	}
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
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
	public float getNum1() {
		return num1;
	}
	public void setNum1(float num1) {
		this.num1 = num1;
	}
	public float getNum2() {
		return num2;
	}
	public void setNum2(float num2) {
		this.num2 = num2;
	}
	public float getResult() {
		return result;
	}
	public void setResult(float result) {
		this.result = result;
	}
	public long getResultTypeId() {
		return resultTypeId;
	}
	public void setResultTypeId(long resultTypeId) {
		this.resultTypeId = resultTypeId;
	}
	public DoubleType getResultType() {
		return resultType;
	}
	public void setResultType(DoubleType resultType) {
		this.resultType = resultType;
	}
}