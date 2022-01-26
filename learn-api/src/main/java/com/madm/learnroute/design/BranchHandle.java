package com.madm.learnroute.design;

@FunctionalInterface
public interface BranchHandle {

    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
