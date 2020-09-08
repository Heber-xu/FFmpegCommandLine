# 适用于Android端的FFmpeg命令行工具

[参考文章](https://www.jianshu.com/p/dfe9404e001a)使用的是ndk-build方式，并且使用中ffmpeg命令会偶现闪退问题，同时也没有64位so文件，所以需要自己修改编译。

# 相关说明

* 本项目使用的是ijkplay项目中生成的libijkffmpeg.so，可自行替换自己编译的ffmpeg.so文件。
* 调用部分命令依然可能闪退，原因是ffmpeg的部分命令行可能需要一些第三方库，比如x264、x265，这就需要我们自己编译的ffmpeg.so文件中集成了相关库才行。

# 注意事项

* demo中部分命令路径都来自我测试手机的路径，请替换成自己的路径。
* 文件操作需要注意Android Q的适配问题。


