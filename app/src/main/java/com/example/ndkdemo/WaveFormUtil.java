package com.example.ndkdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.documentfile.provider.DocumentFile;

public class WaveFormUtil {

    private static final String TAG = "WaveFormUtil";

    public static final int REQUEST_CODE = 100;

    private final Activity mContext;

    public WaveFormUtil(Activity context) {
        mContext = context;
    }

    public void printFilesName(List<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            Log.e(TAG, "file: " + i + ", " + file.getName());
        }
    }

    public void getFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            openDirectory();
        } else {
            String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/waveform";
            List<File> files = listFiles(directoryPath);
            printFilesName(files);
        }
    }

    public List<File> listFiles(String directoryPath) {
        List<File> fileList = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileList.add(file);
                    } else if (file.isDirectory()) {
                        List<File> nestedFiles = listFiles(file.getAbsolutePath());
                        fileList.addAll(nestedFiles);
                    }
                }
            }
        }
        return fileList;
    }


    public DocumentFile[] listFiles(Uri treeUri) {
        DocumentFile rootDirectory = DocumentFile.fromTreeUri(mContext, treeUri);
        DocumentFile[] files;
        if (rootDirectory != null) {
            files = rootDirectory.listFiles();
        } else {
            files = new DocumentFile[]{};
        }
        return files;
    }


    public void openDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        mContext.startActivityForResult(intent, REQUEST_CODE);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri treeUri = data.getData();
            DocumentFile[] fileList = listFiles(treeUri);
            for (DocumentFile file : fileList) {
                Uri uri = file.getUri();
                int lineCount = getLineCount(uri);
                Log.e(TAG, "lineCount: " + lineCount);
                byte[] bytes1 = readFile(uri);
                Log.e(TAG, "file content: " + new String(bytes1));
            }
        }
    }*/

    public byte[] getFileContentByteArray(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String line;
            while ((line = reader.readLine()) != null) {
                // 将每行内容转换为字节数组
                byte[] lineBytes = line.getBytes();
                // 将字节数组写入 ByteArrayOutputStream
                outputStream.write(lineBytes);
                // 添加换行符
                outputStream.write('\n');
            }
            reader.close();
            inputStream.close();

            // 获取最终的字节数组
            byte[] fileBytes = outputStream.toByteArray();
            outputStream.close();
            return fileBytes;
            // 使用文件字节数组进行后续操作
            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public int getFileContentLineCount(File file) {
        int lineCount = 0;
        try {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (reader.readLine() != null) {
                lineCount++;
            }
            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public byte[] getFileContentByteArray(Uri uri) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                String line;
                while ((line = reader.readLine()) != null) {
                    // 将每行内容转换为字节数组
                    byte[] lineBytes = line.getBytes();
                    // 将字节数组写入 ByteArrayOutputStream
                    outputStream.write(lineBytes);
                    // 添加换行符
                    outputStream.write('\n');
                }
                reader.close();
                inputStream.close();

                // 获取最终的字节数组
                byte[] fileBytes = outputStream.toByteArray();
                outputStream.close();
                return fileBytes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }

    public int getFileContentLineCount(Uri uri) {
        int lineCount = 0;
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while (reader.readLine() != null) {
                    lineCount++;
                }
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }
}
