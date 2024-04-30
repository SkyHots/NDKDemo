package com.example.ndkdemo;

import java.util.ArrayList;

public class TCRecord {

    int result;
    short play_times;
    ArrayList<PlayRecord> play_record_list;

    @Override
    public String toString() {
        return "TCRecord{" +
                "play_times=" + play_times +
                ", play_record_list=" + play_record_list.toString() +
                '}';
    }
}
