package com.example.ndkdemo;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.ndkdemo.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String name;

    private ActivityMainBinding binding;
    private Jni mJni = new Jni();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String path = Environment.getRootDirectory().getAbsolutePath() + "/etc/public.libraries.txt";
        File file = new File(path);
        if (file.exists()) {
            Log.e(TAG, FileContentReader.readFileContent(path));
        }


        TCRecord tcRecord = new TCRecord();
        tcRecord.play_times = 4;
        ArrayList<PlayRecord> playRecordList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            playRecordList.add(new PlayRecord((short) 0, 1, 2, 3));
        }
        tcRecord.play_record_list = playRecordList;
        binding.textView.setText(new Gson().toJson(mJni.sfdc_tc_execute("haha", tcRecord)));

        Student student = new Student();
        student.setAge(18);
        Log.e(TAG, "getStudentList: " + new Gson().toJson(mJni.getStudentList(student, 5)));
        Log.e(TAG, "stringFromJNI: " + mJni.stringFromJNI());
        Log.e(TAG, "getByte: " + Arrays.toString(mJni.getByte(new byte[]{1, 2, 3, 4, 5, 6, 7})));

        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Student stu = new Student();
            stu.setName("张三");
            stu.setAge(18);
            students.add(stu);
        }
        Log.e(TAG, "tranListToArray: " + Arrays.toString(mJni.tranListToArray(students)));

        Student stu = new Student();
        stu.setName("444");
        stu.setAge(12);
        Log.e(TAG, "Student: " + new Gson().toJson(mJni.changeStudentName(stu)));
    }


}