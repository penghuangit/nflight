package com.abreqadhabra.nflight.samples.socket.stream;

import java.io.Serializable;

class EmployeeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int empID;
	String empName;
	double empSalary;

	void setID(int id) {
		empID = id;
	}

	void setName(String name) {
		empName = name;
	}

	void setSalary(double salary) {
		empSalary = salary;
	}

	int getID() {
		return empID;
	}

	String getName() {
		return empName;
	}

	double getSalary() {
		return empSalary;
	}
}