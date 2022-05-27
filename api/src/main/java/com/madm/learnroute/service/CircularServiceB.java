package com.madm.learnroute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CircularServiceB {
	@Autowired
	private CircularServiceA circularServiceA;

	public void methodB() {
		System.out.println("方法 B 执行");
	}
}
