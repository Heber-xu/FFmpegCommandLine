#include <jni.h>
#include "ffmpeg.h"

#define TAG "ffmpegcommand_c"

//
// Created by 徐航 on 2020/9/8.
//

JNIEXPORT jint JNICALL
Java_com_xh_command_FFmpegCommand_execute(JNIEnv *env, jclass clazz, jobjectArray commands) {

    int argc = (*env)->GetArrayLength(env, commands);
    char *argv[argc];

    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        argv[i] = (char*) (*env)->GetStringUTFChars(env, js, 0);
    }
    return run(argc, argv);

}