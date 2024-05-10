package com.example.ndkdemo;

import java.util.ArrayList;

public class Jni {

    static {
        System.loadLibrary("ndkdemo");
    }

    public native TCRecord sfdc_tc_execute(String name, TCRecord record);

    public native ArrayList<Student> getStudentList(Student student, int size);

    public native String stringFromJNI();

    public native byte[] getByte(byte[] bytes);

    public native Student changeStudentName(Student student);

    public native Student[] tranListToArray(ArrayList<Student> students);
}
