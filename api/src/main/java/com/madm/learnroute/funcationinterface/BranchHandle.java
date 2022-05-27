package com.madm.learnroute.funcationinterface;

@FunctionalInterface
public interface BranchHandle {

    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
