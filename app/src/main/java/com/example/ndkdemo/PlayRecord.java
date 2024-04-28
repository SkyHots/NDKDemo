package com.example.ndkdemo;

public class PlayRecord {

    short wave_index;
    float temperature;
    float output_f0;
    float relative_bemf;

    public PlayRecord(short wave_index, float temperature, float output_f0, float relative_bemf) {
        this.wave_index = wave_index;
        this.temperature = temperature;
        this.output_f0 = output_f0;
        this.relative_bemf = relative_bemf;
    }

    @Override
    public String toString() {
        return "PlayRecord{" +
                "wave_index=" + wave_index +
                ", temperature=" + temperature +
                ", output_f0=" + output_f0 +
                ", relative_bemf=" + relative_bemf +
                '}';
    }

    public PlayRecord() {
    }
}
