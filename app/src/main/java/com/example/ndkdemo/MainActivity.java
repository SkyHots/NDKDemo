package com.example.ndkdemo;

import android.os.Bundle;

import com.example.ndkdemo.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String name;

    static {
        System.loadLibrary("ndkdemo");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TCRecord tcRecord = new TCRecord();
        tcRecord.play_times = 4;
        ArrayList<PlayRecord> playRecordList = new ArrayList<>();
        playRecordList.add(new PlayRecord((short) 0, 1, 2, 3));
        tcRecord.play_record_list = playRecordList;
        binding.textView.setText(new Gson().toJson(sfdc_tc_execute("haha", tcRecord, 9)));


    }

    public native TCRecord sfdc_tc_execute(String name, TCRecord record, int waveSize);

    public native List<Student> getStudent(Student student);

    public native String stringFromJNI();

    public native byte[] getByte(byte[] bytes);

    public native void changeName();

}