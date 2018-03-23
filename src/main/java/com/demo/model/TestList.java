package com.demo.model;

import java.io.Serializable;
import java.util.List;


public class TestList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private List<TestModel> data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TestModel> getData() {
		return data;
	}

	public void setData(List<TestModel> data) {
		this.data = data;
	}
}
