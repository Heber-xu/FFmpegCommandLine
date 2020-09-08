//
// Created by minieye on 2020/7/10.
//

#ifndef CAMERARECODER_LOGGER_H
#define CAMERARECODER_LOGGER_H

#include <android/log.h>

#define LOGI(TAG, ...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGD(TAG, ...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define LOGE(TAG, ...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#endif //CAMERARECODER_LOGGER_H

