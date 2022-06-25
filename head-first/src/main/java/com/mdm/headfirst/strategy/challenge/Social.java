package com.mdm.headfirst.strategy.challenge;

import com.mdm.headfirst.strategy.challenge.ShareStrategy;

public class Social implements ShareStrategy {
	public void share() {
		System.out.println("I'm posting the photo on social media");
	}
}
