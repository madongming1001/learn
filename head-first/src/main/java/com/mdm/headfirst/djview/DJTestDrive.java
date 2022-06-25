package com.mdm.headfirst.djview;

import com.mdm.headfirst.djview.BeatController;
import com.mdm.headfirst.djview.BeatModelInterface;
import com.mdm.headfirst.djview.ControllerInterface;

public class DJTestDrive {

    public static void main (String[] args) {
        BeatModelInterface model = new BeatModel();
		ControllerInterface controller = new BeatController(model);
    }
}
