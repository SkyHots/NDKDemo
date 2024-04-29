#include <jni.h>
#include <string>
#include <android/log.h>

struct student {
    char *name;
    int32_t age;
};

extern "C" JNIEXPORT jobject

JNICALL
Java_com_example_ndkdemo_MainActivity_getStudentList(JNIEnv *env, jobject thiz, jobject student, jint len) {
    // 获取 Student 类的引用
    jclass studentClass = env->GetObjectClass(student);

    // 获取 name 和 age 字段的 ID
    jfieldID nameFieldID = env->GetFieldID(studentClass, "name", "Ljava/lang/String;");
    jfieldID ageFieldID = env->GetFieldID(studentClass, "age", "I");

    // 创建一个 ArrayList 对象
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jobject arrayListObj = env->NewObject(arrayListClass, arrayListConstructor);

    // 获取 ArrayList 的 add 方法的 ID
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    // 获取原始姓名和年龄值
    jstring nameValue = static_cast<jstring>(env->GetObjectField(student, nameFieldID));

    const char *nameChars = env->GetStringUTFChars(nameValue, nullptr);
    std::string nameString(nameChars);
    std::string newString = nameString + "aaa";

    env->ReleaseStringUTFChars(nameValue, nameChars);

    jstring newNameValue = env->NewStringUTF(newString.c_str());
    jint ageValue = env->GetIntField(student, ageFieldID) + 1;

    // 创建两个新的 Student 对象，并设置相同的姓名和年龄
    for (int i = 0; i < len; i++) {
        // 创建新的 Student 对象
        jobject newStudent = env->NewObject(studentClass, env->GetMethodID(studentClass, "<init>", "()V"));

        // 设置姓名和年龄字段值
        env->SetObjectField(newStudent, nameFieldID, newNameValue);
        env->SetIntField(newStudent, ageFieldID, ageValue);

        // 将新的 Student 对象添加到 ArrayList 中
        env->CallBooleanMethod(arrayListObj, arrayListAddMethod, newStudent);
    }

    return arrayListObj;
}

extern "C" JNIEXPORT jstring
JNICALL
Java_com_example_ndkdemo_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    jstring result = env->NewStringUTF("Hello from C++");
    return
            result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ndkdemo_MainActivity_changeName(JNIEnv *env, jobject thiz) {
    jclass clazz = env->GetObjectClass(thiz);
    jfieldID name = env->GetFieldID(clazz, "name", "Ljava/lang/String;");
    env->SetObjectField(thiz, name, env->NewStringUTF("MainActivity"));
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_ndkdemo_MainActivity_getByte(JNIEnv *env, jobject thiz, jbyteArray bytes) {
    jsize jsize1 = env->GetArrayLength(bytes);
    jbyte *data = env->GetByteArrayElements(bytes, nullptr);
    for (int i = 0; i < jsize1; ++i) {
        data[i] = data[i] + 1;
    }
    jbyteArray result = env->NewByteArray(jsize1);
    env->SetByteArrayRegion(result, 0, jsize1, data);
    env->ReleaseByteArrayElements(bytes, data, 0);
    return result;
}

struct tc_record {
    uint16_t play_times;
    struct play_record *play_record_list;
};

struct play_record {
    uint16_t wave_index;
    float temperature;
    float output_f0;
    float relative_bemf;
};

int32_t sfdc_tc_execute(const char *tc_name, struct tc_record *record, uint16_t size) {
    // 假设返回的play_times为2
    record->play_times = 2;
    // 动态分配存储play_records的内存
    struct play_record *play_records = (struct play_record *) malloc(record->play_times * sizeof(struct play_record));
    for (int i = 0; i < size; ++i) {
        play_records[i].wave_index = i;
        play_records[i].temperature = i + 1;
        play_records[i].output_f0 = i + 2;
        play_records[i].relative_bemf = i + 3;
    }
    // 将play_record_list指针指向动态分配的内存
    record->play_record_list = play_records;
    return 1;
}

extern "C" {
JNIEXPORT jobject JNICALL
Java_com_example_ndkdemo_MainActivity_sfdc_1tc_1execute(JNIEnv *env, jobject obj, jstring tcName, jobject recordObject,
                                                        jint waveSize) {
    const char *tc_name = env->GetStringUTFChars(tcName, NULL);
    struct tc_record record;
    jint result = sfdc_tc_execute(tc_name, &record, waveSize);

    for (int i = 0; i < waveSize; ++i) {
        __android_log_print(ANDROID_LOG_ERROR, "tag", "%hu", record.play_times);
        __android_log_print(ANDROID_LOG_ERROR, "tag", "%d", result);
        __android_log_print(ANDROID_LOG_ERROR, "tag", "wave_index: %hu", record.play_record_list[i].wave_index);
        __android_log_print(ANDROID_LOG_ERROR, "tag", "temperature: %f", record.play_record_list[i].temperature);
        __android_log_print(ANDROID_LOG_ERROR, "tag", "output_f0: %f", record.play_record_list[i].output_f0);
        __android_log_print(ANDROID_LOG_ERROR, "tag", "relative_bemf: %f", record.play_record_list[i].relative_bemf);
    }

    // 获取Java类的引用
    jclass recordClass = env->GetObjectClass(recordObject);

    // 获取Java类中字段的引用
    jfieldID playTimesField = env->GetFieldID(recordClass, "play_times", "S");
    jfieldID playRecordListField = env->GetFieldID(recordClass, "play_record_list", "Ljava/util/ArrayList;");

    // 创建Java数组对象
    jclass playRecordClass = env->FindClass("com/example/ndkdemo/PlayRecord");
    jobjectArray playRecordArray = env->NewObjectArray(waveSize, playRecordClass, NULL);

    // 设置playTimes字段的值
    env->SetShortField(recordObject, playTimesField, record.play_times);

    // 设置playRecordList字段的值
    for (int i = 0; i < waveSize; ++i) {
        // 创建PlayRecord对象
        jobject playRecordObject = env->NewObject(playRecordClass, env->GetMethodID(playRecordClass, "<init>", "()V"));

        // 获取PlayRecord类中字段的引用
        jfieldID waveIndexField = env->GetFieldID(playRecordClass, "wave_index", "S");
        jfieldID temperatureField = env->GetFieldID(playRecordClass, "temperature", "F");
        jfieldID outputF0Field = env->GetFieldID(playRecordClass, "output_f0", "F");
        jfieldID relativeBemfField = env->GetFieldID(playRecordClass, "relative_bemf", "F");

        // 设置PlayRecord对象的字段值
        env->SetShortField(playRecordObject, waveIndexField, record.play_record_list[i].wave_index);
        env->SetFloatField(playRecordObject, temperatureField, record.play_record_list[i].temperature);
        env->SetFloatField(playRecordObject, outputF0Field, record.play_record_list[i].output_f0);
        env->SetFloatField(playRecordObject, relativeBemfField, record.play_record_list[i].relative_bemf);

        // 将PlayRecord对象添加到数组中
        env->SetObjectArrayElement(playRecordArray, i, playRecordObject);

        // 释放PlayRecord对象的引用
        env->DeleteLocalRef(playRecordObject);
    }

    // 获取ArrayList类的引用
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID arrayListCtor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    // 创建一个新的ArrayList对象
    jobject arrayListObj = env->NewObject(arrayListClass, arrayListCtor);

    // 获取jobjectArray的长度
    jsize length = env->GetArrayLength(playRecordArray);

    // 遍历jobjectArray，将每个元素添加到ArrayList中
    for (jsize i = 0; i < length; ++i) {
        jobject element = env->GetObjectArrayElement(playRecordArray, i);
        env->CallBooleanMethod(arrayListObj, arrayListAdd, element);
        env->DeleteLocalRef(element);
    }

    // 释放引用
    env->DeleteLocalRef(arrayListClass);

    // 设置playRecordList字段的值
    env->SetObjectField(recordObject, playRecordListField, arrayListObj);

    // 释放引用
    env->DeleteLocalRef(playRecordArray);
    env->DeleteLocalRef(playRecordClass);
    env->ReleaseStringUTFChars(tcName, tc_name);

    // 返回Java对象
    return recordObject;
}
}
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_ndkdemo_MainActivity_tranListToArray(JNIEnv *env, jobject thiz, jobject students) {
    // 获取Student类的引用
    jclass studentClass = env->FindClass("com/example/ndkdemo/Student");

    // 获取ArrayList类的引用和get和size方法的ID
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jmethodID getMethodId = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID sizeMethodId = env->GetMethodID(arrayListClass, "size", "()I");
    jint size = env->CallIntMethod(students, sizeMethodId);
    // 创建Student[]数组
    jobjectArray studentArray = env->NewObjectArray(size, studentClass, NULL);
    // 遍历ArrayList，将每个Student对象添加到Student[]数组中
    for (int i = 0; i < size; i++) {
        jobject studentObject = env->CallObjectMethod(students, getMethodId, i);
        env->SetObjectArrayElement(studentArray, i, studentObject);
        env->DeleteLocalRef(studentObject);
    }
    // 返回转换后的Student[]数组
    return studentArray;
}