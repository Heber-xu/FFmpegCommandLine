//
// Created by 徐航 on 2020/11/3.
//

#include "libavutil/timestamp.h"
#include "libavformat/avformat.h"

#include "logger.h"

#define TAG "remux.c"


int remux(const char *input_file, const char *output_file) {

    AVFormatContext *input_format_context = NULL, *output_format_context = NULL;
    const char *in_filename, *out_filename;
    AVPacket packet;
    int ret, i;
    int stream_index = 0;
    int *streams_list = NULL;
    int number_of_streams = 0;
    // 标志输出的文件是否是fmp4：0-不是，1-是。
    int fragmented_mp4_options = 0;

    in_filename = input_file;
    out_filename = output_file;

    LOGI(TAG, "in_filename : %s , out_filename : %s", in_filename, out_filename);

    av_register_all();

    if ((ret = avformat_open_input(&input_format_context, in_filename, NULL, NULL)) < 0) {
        LOGE(TAG, "Could not open input file %s , ret : %d", in_filename, ret);
        goto end;
    }
    if ((ret = avformat_find_stream_info(input_format_context, NULL)) < 0) {
        LOGE(TAG, "Failed to retrieve input stream information");
        goto end;
    }

    avformat_alloc_output_context2(&output_format_context, NULL, NULL, out_filename);
    if (!output_format_context) {
        LOGE(TAG, "Could not create output context\n");
        ret = AVERROR_UNKNOWN;
        goto end;
    }

    number_of_streams = input_format_context->nb_streams;
    streams_list = av_mallocz_array(number_of_streams, sizeof(*streams_list));

    if (!streams_list) {
        ret = AVERROR(ENOMEM);
        goto end;
    }

    for (i = 0; i < input_format_context->nb_streams; i++) {

        AVStream *out_stream;
        AVStream *in_stream = input_format_context->streams[i];

        AVCodecParameters *in_codecpar = in_stream->codecpar;
        if (in_codecpar->codec_type != AVMEDIA_TYPE_AUDIO &&
            in_codecpar->codec_type != AVMEDIA_TYPE_VIDEO &&
            in_codecpar->codec_type != AVMEDIA_TYPE_SUBTITLE) {
            streams_list[i] = -1;
            continue;
        }
        streams_list[i] = stream_index++;
        out_stream = avformat_new_stream(output_format_context, NULL);
        if (!out_stream) {
            fprintf(stderr, "Failed allocating output stream\n");
            ret = AVERROR_UNKNOWN;
            goto end;
        }

        // 修改 subtitle 编码信息
        // 实际上这里无法修改，对于mp4文件 subtitle 只支持 mov_text，参考：
        // https://stackoverflow.com/questions/54960500/could-not-find-tag-for-codec-subrip-in-stream-codec-not-currently-supported-in
        // if(in_stream->codecpar->codec_type == AVMEDIA_TYPE_SUBTITLE){
        //   in_codecpar->codec_id = AV_CODEC_ID_TEXT;
        // }

        //这里必须要这样做，不然会无法remux，在写文件头的时候报错：Tag text incompatible with output codec id '94213' (tx3g)
        in_codecpar->codec_tag = 0;
        ret = avcodec_parameters_copy(out_stream->codecpar, in_codecpar);
        if (ret < 0) {
            fprintf(stderr, "Failed to copy codec parameters\n");
            goto end;
        }
        out_stream->id = in_stream->id;
    }
    // https://ffmpeg.org/doxygen/trunk/group__lavf__misc.html#gae2645941f2dc779c307eb6314fd39f10
    fprintf(stderr, "av_dump_format start\n");
    av_dump_format(output_format_context, 0, out_filename, 1);
    fprintf(stderr, "av_dump_format end\n");
    // unless it's a no file (we'll talk later about that) write to the disk (FLAG_WRITE)
    // but basically it's a way to save the file to a buffer so you can store it
    // wherever you want.
    if (!(output_format_context->oformat->flags & AVFMT_NOFILE)) {
        ret = avio_open(&output_format_context->pb, out_filename, AVIO_FLAG_WRITE);
        if (ret < 0) {
            fprintf(stderr, "Could not open output file '%s'\n", out_filename);
            goto end;
        }
    }

    AVDictionary *opts = NULL;

    if (fragmented_mp4_options) {
        // https://developer.mozilla.org/en-US/docs/Web/API/Media_Source_Extensions_API/Transcoding_assets_for_MSE
        av_dict_set(&opts, "movflags", "frag_keyframe+empty_moov+default_base_moof", 0);
    }
    // https://ffmpeg.org/doxygen/trunk/group__lavf__encoding.html#ga18b7b10bb5b94c4842de18166bc677cb
    ret = avformat_write_header(output_format_context, &opts);
    if (ret < 0) {
        fprintf(stderr, "Error occurred when opening output file : %d\n", ret);
        goto end;
    }

    while (1) {
        AVStream *in_stream, *out_stream;
        ret = av_read_frame(input_format_context, &packet);
        if (ret < 0)
            break;

        in_stream = input_format_context->streams[packet.stream_index];
        if (packet.stream_index >= number_of_streams || streams_list[packet.stream_index] < 0) {
            av_packet_unref(&packet);
            continue;
        }
        packet.stream_index = streams_list[packet.stream_index];
        out_stream = output_format_context->streams[packet.stream_index];
        /* copy packet */
        packet.pts = av_rescale_q_rnd(packet.pts, in_stream->time_base, out_stream->time_base,
                                      AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);
        packet.dts = av_rescale_q_rnd(packet.dts, in_stream->time_base, out_stream->time_base,
                                      AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX);
        packet.duration = av_rescale_q(packet.duration, in_stream->time_base,
                                       out_stream->time_base);
        // https://ffmpeg.org/doxygen/trunk/structAVPacket.html#ab5793d8195cf4789dfb3913b7a693903
        packet.pos = -1;

        //https://ffmpeg.org/doxygen/trunk/group__lavf__encoding.html#ga37352ed2c63493c38219d935e71db6c1
        ret = av_interleaved_write_frame(output_format_context, &packet);
        if (ret < 0) {
            fprintf(stderr, "Error muxing packet\n");
            break;
        }
        av_packet_unref(&packet);
    }
    //https://ffmpeg.org/doxygen/trunk/group__lavf__encoding.html#ga7f14007e7dc8f481f054b21614dfec13
    av_write_trailer(output_format_context);
    end:
    avformat_close_input(&input_format_context);
    /* close output */
    if (output_format_context && !(output_format_context->oformat->flags & AVFMT_NOFILE))
        avio_closep(&output_format_context->pb);
    avformat_free_context(output_format_context);
    av_freep(&streams_list);
    if (ret < 0 && ret != AVERROR_EOF) {
        fprintf(stderr, "Error occurred: %s\n", av_err2str(ret));
        return 1;
    }
    return 0;
}
