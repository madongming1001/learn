package com.mdm.headfirst.strategy.challenge;

import com.mdm.headfirst.strategy.challenge.ShareStrategy;

public class Email implements ShareStrategy {
	public void share() {
		System.out.println("I'm emailing the photo");
	}
}
