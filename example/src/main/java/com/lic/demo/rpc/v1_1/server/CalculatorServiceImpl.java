package com.lic.demo.rpc.v1_1.server;

import com.lic.demo.rpc.v1_1.common.CalculatorService;

public class CalculatorServiceImpl implements CalculatorService {

	@Override
	public double add(double op1, double op2, double op3) {
		return op1 + op2 + op3;
	}

	@Override
	public double substract(double op1, double op2) {
		return op1 - op2;
	}

	@Override
	public double multiply(double op1, double op2) {
		return op1 * op2;
	}

}
