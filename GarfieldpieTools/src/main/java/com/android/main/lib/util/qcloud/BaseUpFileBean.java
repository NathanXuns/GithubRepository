package com.android.main.lib.util.qcloud;

/**
 * 上传文件基础Bean
 */

public class BaseUpFileBean {

    public enum FileType {
        File,
        Photo,
        Audio,
        Video,
        Other
    }

    protected FileType fileType; // 文件类型
    protected String fileLocalPath; // 文件本地地址
    protected String fileServerPath; // 文件服务器地址

    public BaseUpFileBean(FileType fileType, String fileLocalPath, String fileServerPath) {
        this.fileType = fileType;
        this.fileLocalPath = fileLocalPath;
        this.fileServerPath = fileServerPath;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }


    public static class BaseUpFileBeanBuilder {
        private FileType fileType;
        private String fileLocalPath;
        private String fileServerPath;

        public BaseUpFileBeanBuilder setFileType(FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        public BaseUpFileBeanBuilder setFileLocalPath(String fileLocalPath) {
            this.fileLocalPath = fileLocalPath;
            return this;
        }

        public BaseUpFileBeanBuilder setFileServerPath(String fileServerPath) {
            this.fileServerPath = fileServerPath;
            return this;
        }

        public BaseUpFileBean createBaseUpFileBean() {
            return new BaseUpFileBean(fileType, fileLocalPath, fileServerPath);
        }
    }
}
