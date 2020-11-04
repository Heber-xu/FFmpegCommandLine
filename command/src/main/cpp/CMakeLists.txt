cmake_minimum_required(VERSION 3.4.1)

# 因为需要导入大量的头文件，引用起来不是很方便，所以这里直接包含外部文件
include_directories(/Users/xuhang/Projects/ffmpeg/ffmpeg-3.3.2)

add_library(ffmpegcommand
        SHARED
        ffmpegcommand.c
        cmdutils.c
        ffmpeg.c
        ffmpeg_filter.c
        ffmpeg_opt.c
        remux.c)

add_library(ijkffmpeg SHARED IMPORTED)

set_target_properties(ijkffmpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libijkffmpeg.so)

target_link_libraries(
        ffmpegcommand
        ijkffmpeg
        android
        log)