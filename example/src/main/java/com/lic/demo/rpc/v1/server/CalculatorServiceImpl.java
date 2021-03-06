package com.lic.demo.rpc.v1.server;

import com.lic.demo.rpc.v1.common.CalculatorService;

public class CalculatorServiceImpl implements CalculatorService {

	@Override
	public double add(double op1, double op2) {
		return op1 + op2;
	}

	@Override
	public double substract(double op1, double op2) {
		return op1 - op2;
	}

	@Override
	public double multiply(double op1, double op2) {
		return op1 * op2;
	}
	
	public static void main(String[] args) {
		CalculatorService service = new CalculatorServiceImpl();
		System.out.println(service.add(1.0, 2.0));
	}

}
