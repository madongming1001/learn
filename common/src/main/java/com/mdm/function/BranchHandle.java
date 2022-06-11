package com.mdm.function;

@FunctionalInterface
public interface BranchHandle {

    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
