package com.madm.learnroute.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CircularServiceA {
	private String fieldA = "字段 A";

	@Autowired
	private CircularServiceB circularServiceB;

	public void methodA() {
		System.out.println("方法 A 执行");
	}
}