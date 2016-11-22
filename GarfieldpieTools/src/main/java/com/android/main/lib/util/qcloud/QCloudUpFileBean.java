package com.android.main.lib.util.qcloud;

import com.tencent.upload.Const;
import com.tencent.upload.task.Dentry;

/**
 * 上传文件Bean
 */

public class QCloudUpFileBean {
    private String fileLocalPath; // 文件本地地址
    private Const.FileType fileType; // 文件类型
    private Dentry destDentry; // 目标目录
    private Object externalData; // 外部数据

    public QCloudUpFileBean(String fileLocalPath, Const.FileType fileType, Dentry destDentry, Object externalData) {
        this.fileLocalPath = fileLocalPath;
        this.fileType = fileType;
        this.destDentry = destDentry;
        this.externalData = externalData;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public Const.FileType getFileType() {
        return fileType;
    }

    public void setFileType(Const.FileType fileType) {
        this.fileType = fileType;
    }

    public Dentry getDestDentry() {
        return destDentry;
    }

    public void setDestDentry(Dentry destDentry) {
        this.destDentry = destDentry;
    }

    public Object getExternalData() {
        return externalData;
    }

    public void setExternalData(Object externalData) {
        this.externalData = externalData;
    }


    public static class QCloundUpFileBeanBuilder {
        private String fileLocalPath;
        private Const.FileType fileType;
        private Dentry destDentry;
        private Object externalData;

        public QCloundUpFileBeanBuilder setFileLocalPath(String fileLocalPath) {
            this.fileLocalPath = fileLocalPath;
            return this;
        }

        public QCloundUpFileBeanBuilder setFileType(Const.FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        public QCloundUpFileBeanBuilder setDestDentry(Dentry destDentry) {
            this.destDentry = destDentry;
            return this;
        }

        public QCloundUpFileBeanBuilder setExternalData(Object externalData) {
            this.externalData = externalData;
            return this;
        }

        public QCloudUpFileBean createQCloundUpFileBean() {
            return new QCloudUpFileBean(fileLocalPath, fileType, destDentry, externalData);
        }
    }
}
