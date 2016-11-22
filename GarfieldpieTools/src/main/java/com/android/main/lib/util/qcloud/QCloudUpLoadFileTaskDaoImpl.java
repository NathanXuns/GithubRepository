package com.android.main.lib.util.qcloud;

import android.text.TextUtils;

import com.android.main.lib.util.FileHelper;
import com.tencent.upload.Const;
import com.tencent.upload.task.Dentry;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.VideoAttr;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.FileUploadTask;
import com.tencent.upload.task.impl.PhotoUploadTask;
import com.tencent.upload.task.impl.VideoUploadTask;

import java.io.File;
import java.util.List;

/**
 * QCloud 上传Dao
 */

public class QCloudUpLoadFileTaskDaoImpl implements QCloudUpLoadFileTaskDao {

    @Override
    public void upPhoto(final BaseUpFileBean upFileBean,final QCloudUpLoadFileTaskListener listener) {
        QCloudUpLoadFileManager.getInstance().upload(getPhotoUploadTask(upFileBean.getFileLocalPath(), new IUploadTaskListener() {
            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                upFileBean.setFileServerPath(fileInfo.url);
                listener.onUploadSucceed(fileInfo);
            }

            @Override
            public void onUploadFailed(int i, String s) {
                listener.onUploadFailed(i,s);
            }

            @Override
            public void onUploadProgress(long l, long l1) {
                listener.onUploadProgress(l,l1);
            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {
                listener.onUploadStateChange(taskState);
            }
        }));
    }

    @Override
    public void upFile(final BaseUpFileBean upFileBean,final QCloudUpLoadFileTaskListener listener) {
        Const.FileType fileType = upFileBean.getFileType() == BaseUpFileBean.FileType.Video ? Const.FileType.Video:Const.FileType.File;
        QCloudUpLoadFileManager.getInstance().upload(getFileUploadTask(getQCloudFileBean(upFileBean.getFileLocalPath(),upFileBean instanceof VideoUpFileBean ? ((VideoUpFileBean)upFileBean).getVideoCoverUrl():"",fileType),new IUploadTaskListener(){
            @Override
            public void onUploadSucceed(FileInfo fileInfo) {
                upFileBean.setFileServerPath(fileInfo.url);
                listener.onUploadSucceed(fileInfo);
            }

            @Override
            public void onUploadFailed(int i, String s) {
                listener.onUploadFailed(i,s);
            }

            @Override
            public void onUploadProgress(long l, long l1) {
                listener.onUploadProgress(l,l1);
            }

            @Override
            public void onUploadStateChange(ITask.TaskState taskState) {
                listener.onUploadStateChange(taskState);
            }
        }));
    }

    @Override
    public void upPhotos(List<BaseUpFileBean> upFileBeans,final QCloudUpLoadFileTaskListener listener,boolean isCheckServerPath) {
        if (upFileBeans.size()>0) {

            if (isCheckServerPath) {
                int totalNum = 0;
                for (BaseUpFileBean bean : upFileBeans) {
                    if (TextUtils.isEmpty(bean.getFileServerPath())) {
                        totalNum = totalNum + 1;
                    }
                }
                listener.setTotalNum(totalNum);
                if (totalNum == 0) {
                    listener.getListener().onUploadSucceed();
                    return;
                }
            }

            for (final BaseUpFileBean bean :upFileBeans) {

                // 过滤掉已经有服务器URL的
                if (isCheckServerPath && !TextUtils.isEmpty(bean.getFileServerPath())) {
                    continue;
                }

                upPhoto(bean,listener);
            }
        }
    }

    @Override
    public void upFiles(List<BaseUpFileBean> files,final QCloudUpLoadFileTaskListener listener,boolean isCheckServerPath) {
        if (files.size()>0) {

            if (isCheckServerPath) {
                int totalNum = 0;
                for (BaseUpFileBean file : files) {
                    if (TextUtils.isEmpty(file.getFileServerPath())) {
                        totalNum = totalNum + 1;
                    }
                }
                listener.setTotalNum(totalNum);
                if (totalNum == 0) {
                    listener.getListener().onUploadSucceed();
                    return;
                }
            }

            for (final BaseUpFileBean file : files ) {

                // 过滤掉已经有服务器URL的
                if (isCheckServerPath && !TextUtils.isEmpty(file.getFileServerPath())) {
                    continue;
                }

                Const.FileType fileType = Const.FileType.File;

                if (file.getFileType() == BaseUpFileBean.FileType.Video) {
                    fileType = Const.FileType.Video;
                } else if (file.getFileType() == BaseUpFileBean.FileType.File) {
                    fileType = Const.FileType.File;
                } else if (file.getFileType() == BaseUpFileBean.FileType.Photo) {
                    fileType = Const.FileType.Photo;
                }

                // 上传图片
                if (fileType == Const.FileType.Photo) {
                    upPhoto(file,listener);
                }

                // 上传文件
                else {
                    upFile(file,listener);
                }
            }
        }
    }

    /**
     * 获取上传图片Task
     * @param filePath 图片地址
     * @param listener 监听
     * @return
     */
    private PhotoUploadTask getPhotoUploadTask(String filePath, IUploadTaskListener listener){
        PhotoUploadTask task = new PhotoUploadTask(filePath, listener);
//        task.setFileId("test_fileId_" + UUID.randomUUID()); // 可以为图片自定义FileID(可选)
        task.setAuth(getSign(Const.FileType.Photo));
        task.setBucket(QCloudUpLoadFileManager.PHOTO_BUCKET);  // 设置Bucket(命名空间)
        return task;

    }

    /**
     * 获取上传文件Task
     * @param file 文件
     * @param listener 监听
     * @return
     */
    private UploadTask getFileUploadTask(QCloudUpFileBean file, IUploadTaskListener listener){
        UploadTask task = null;
        if (file.getDestDentry().type == Dentry.FILE) {
            task = new FileUploadTask(getBucket(file.getFileType()), file.getFileLocalPath(), file.getDestDentry().path, file.getDestDentry().attribute, listener);
        } else if (file.getDestDentry().type == Dentry.VIDEO) {
            VideoAttr videoAttr = (VideoAttr)file.getExternalData();
            task = new VideoUploadTask(getBucket(file.getFileType()), file.getFileLocalPath(), file.getDestDentry().path, file.getDestDentry().attribute, videoAttr, listener);
        }

        if (task != null) task.setAuth(getSign(file.getFileType()));
        return task;
    }

    @Override
    public QCloudUpFileBean getQCloudFileBean(String filePath, String videoCoverUrl, Const.FileType fileType) {

        Dentry dentry = null;
        VideoAttr videoAttr = null;
        File file = FileHelper.createFile(filePath);
        String destFilePath ;

        if (fileType ==Const.FileType.File) {
            destFilePath = "/audio/" + System.currentTimeMillis() +file.getName();
            dentry = new Dentry(Dentry.FILE).setPath(destFilePath);
        }else if (fileType == Const.FileType.Video) {
            destFilePath = "/show/" + System.currentTimeMillis() +file.getName();
            dentry = new Dentry(Dentry.VIDEO).setPath(destFilePath);

            videoAttr = new VideoAttr();

            videoAttr.isCheck = false;
            videoAttr.title = file.getName();
            videoAttr.desc = "cos-video-desc-" + file.getName();
            videoAttr.coverUrl = videoCoverUrl;
        }

        return new QCloudUpFileBean.QCloundUpFileBeanBuilder().setFileLocalPath(filePath).setFileType(fileType).setDestDentry(dentry).setExternalData(videoAttr).createQCloundUpFileBean();

    }

    /**
     * 获取命名空间
     * @param mFileType
     * @return
     */
    private String getBucket(Const.FileType mFileType) {
        if (mFileType == Const.FileType.File) {
            return QCloudUpLoadFileManager.FILE_BUCKET;
        }

        if (mFileType == Const.FileType.Video) {
            return QCloudUpLoadFileManager.VIDEO_BUCKET;
        }

        return "";
    }

    /**
     * 获取签名
     * @param mFileType
     * @return
     */
    public static String getSign(Const.FileType mFileType) {
        String sign;
        switch (mFileType){
            case File:
                sign = QCloudUpLoadFileManager.FILE_SIGN;
                break;
            case Video:
                sign = QCloudUpLoadFileManager.VIDEO_SIGN;
                break;
            case Photo:
                sign = QCloudUpLoadFileManager.PHOTO_SIGN;
                break;
            default:
                sign = "";
                break;
        }
        return sign;
    }
}
