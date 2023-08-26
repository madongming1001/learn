package com.mdm.headfirst.djview;

public interface BeatModelInterface {
    void initialize();

    void on();

    void off();

    int getBPM();

    void setBPM(int bpm);

    void registerObserver(BeatObserver o);

    void removeObserver(BeatObserver o);

    void registerObserver(BPMObserver o);

    void removeObserver(BPMObserver o);
}
