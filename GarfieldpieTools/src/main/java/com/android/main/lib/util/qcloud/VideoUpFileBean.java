package com.android.main.lib.util.qcloud;

/**
 * 视频上传Bean
 */

public class VideoUpFileBean extends BaseUpFileBean {

    private String videoCoverUrl; // 视频封面URL

    public VideoUpFileBean(FileType fileType, String fileLocalPath, String fileServerPath, String videoCoverUrl) {
        super(fileType, fileLocalPath, fileServerPath);
        this.videoCoverUrl = videoCoverUrl;
    }

    public String getVideoCoverUrl() {
        return videoCoverUrl;
    }

    public void setVideoCoverUrl(String videoCoverUrl) {
        this.videoCoverUrl = videoCoverUrl;
    }


    public static class VideoUpFileBeanBuilder {
        private FileType fileType;
        private String fileLocalPath;
        private String fileServerPath;
        private String videoCoverUrl;

        public VideoUpFileBeanBuilder setFileType(FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        public VideoUpFileBeanBuilder setFileLocalPath(String fileLocalPath) {
            this.fileLocalPath = fileLocalPath;
            return this;
        }

        public VideoUpFileBeanBuilder setFileServerPath(String fileServerPath) {
            this.fileServerPath = fileServerPath;
            return this;
        }

        public VideoUpFileBeanBuilder setVideoCoverUrl(String videoCoverUrl) {
            this.videoCoverUrl = videoCoverUrl;
            return this;
        }

        public VideoUpFileBean createVideoUpFileBean() {
            return new VideoUpFileBean(fileType, fileLocalPath, fileServerPath, videoCoverUrl);
        }
    }
}
