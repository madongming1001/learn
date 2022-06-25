package com.mdm.headfirst.djview;

import com.mdm.headfirst.djview.ControllerInterface;
import com.mdm.headfirst.djview.HeartController;

public class HeartTestDrive {

    public static void main (String[] args) {
		HeartModel heartModel = new HeartModel();
        ControllerInterface model = new HeartController(heartModel);
    }
}
