package com.example.ndkdemo;

import android.os.Bundle;
import android.util.Log;

import com.example.ndkdemo.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

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
        binding.textView.setText(new Gson().toJson(sfdc_tc_execute("haha", tcRecord, 1)));

        Student student = new Student();
        student.setName("");
        student.setAge(18);
        Log.e(TAG, "getStudent: " + new Gson().toJson(getStudentList(student, 5)));
        Log.e(TAG, "stringFromJNI: " + stringFromJNI());
        Log.e(TAG, "getByte: " + Arrays.toString(getByte(new byte[]{1, 2, 3, 4, 5, 6, 7})));

        Log.e(TAG, "name: " + name);
        changeName();
        Log.e(TAG, "after changed name: " + name);

        ArrayList<Student> students = new ArrayList<>();
        Student stu = new Student();
        stu.setName("张三");
        stu.setAge(18);
        students.add(stu);
        Log.e(TAG, "tranListToArray: " + Arrays.toString(tranListToArray(students, students.size())));

    }

    public native TCRecord sfdc_tc_execute(String name, TCRecord record, int waveSize);

    public native ArrayList<Student> getStudentList(Student student, int size);

    public native String stringFromJNI();

    public native byte[] getByte(byte[] bytes);

    public native void changeName();

    public native Student[] tranListToArray(ArrayList<Student> students, int size);


}