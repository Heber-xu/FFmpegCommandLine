#include <jni.h>
#include "ffmpeg.h"
#include "remux.h"

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
        argv[i] = (char *) (*env)->GetStringUTFChars(env, js, 0);
    }
    return run(argc, argv);

}

JNIEXPORT jint JNICALL
Java_com_xh_command_FFmpegNotCommand_remux(JNIEnv *env, jclass clazz, jstring input_file,
                                           jstring output_file) {

    const char *input = (*env)->GetStringUTFChars(env, input_file, 0);
    const char *output = (*env)->GetStringUTFChars(env, output_file, 0);

    return remux(input,output);
}