package com.mdm.headfirst.command.undo;

public interface Command {
    public void execute();

    public void undo();
}
