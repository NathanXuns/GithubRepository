package com.android.main.lib.util.qcloud;

import com.tencent.upload.Const;

import java.util.List;

/**
 * 万象优图上传Dao
 */

public interface QCloudUpLoadFileTaskDao {

    void upPhoto(BaseUpFileBean upFileBean, QCloudUpLoadFileTaskListener listener);

    void upPhotos(List<BaseUpFileBean> upFileBeans, QCloudUpLoadFileTaskListener listener,boolean isCheckServerPath);

    void upFile(BaseUpFileBean upFileBean,QCloudUpLoadFileTaskListener listener);

    /**
     * 上传文件
     * @param files 文件
     * @param listener 监听
     * @param isCheckServerPath 是否需要过滤掉已有服务器URL的文件
     */
    void upFiles(List<BaseUpFileBean> files,QCloudUpLoadFileTaskListener listener,boolean isCheckServerPath);

    QCloudUpFileBean getQCloudFileBean(String filePath, String videoCoverUrl, Const.FileType fileType);
}
