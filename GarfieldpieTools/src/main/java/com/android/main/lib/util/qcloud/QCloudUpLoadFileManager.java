package com.android.main.lib.util.qcloud;

import android.content.Context;
import android.text.TextUtils;

import com.android.main.lib.util.SPUtils;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.CommandTask;
import com.tencent.upload.task.UploadTask;

/**
 * 万象优图文件上传Manager
 */

public class QCloudUpLoadFileManager {

    private static class DefaultCfg {
        private static final String APPID = "10067250"; // 腾讯云APPID
        private static final String FILE_BUCKET = "garfieldpie"; // 文件空间Bucket
        private static final String PHOTO_BUCKET = "gpresize"; // 万象优图 - 图片空间
        private static final String VIDEO_BUCKET = "gpshow"; // 视频空间Bucket
    }

    public static String APPID = DefaultCfg.APPID;
    public static String FILE_BUCKET = DefaultCfg.FILE_BUCKET;
    public static String PHOTO_BUCKET = DefaultCfg.PHOTO_BUCKET;
    public static String VIDEO_BUCKET = DefaultCfg.VIDEO_BUCKET;

    public static String FILE_SIGN = "";
    public static String PHOTO_SIGN = "";
    public static String VIDEO_SIGN = "";

    /*签名保存KEY*/
    public static final String SPK_APP_ID = "SPK_APP_ID";
    public static final String SPK_File_Bucket = "SPK_File_Bucket";
    public static final String SPK_Photo_Bucket = "SPK_Photo_Bucket";
    public static final String SPK_Video_Bucket = "SPK_Video_Bucket";

    public static final String SPK_File_Sign = "SPK_File_Sign";
    public static final String SPK_Photo_Sign = "SPK_Photo_Sign";
    public static final String SPK_Video_Sign = "SPK_Video_Sign";

    private UploadManager mFileUploadManager;
    private UploadManager mPhotoUploadManager;
    private UploadManager mVideoUploadManager;

    private Context mContext;

    private static QCloudUpLoadFileManager mQCloudUpLoadFileManager;
    private static final byte[] INSTANCE_LOCK = new byte[0];

    public static synchronized QCloudUpLoadFileManager getInstance() {
        if (mQCloudUpLoadFileManager == null) {
            synchronized (INSTANCE_LOCK) {
                if (mQCloudUpLoadFileManager == null) {
                    mQCloudUpLoadFileManager = new QCloudUpLoadFileManager();
                }
            }
        }
        return mQCloudUpLoadFileManager;
    }

    public void init(Context context) {
        mContext = context;

        loadSignFromSharedPreferencesToMemory();

        getUploadManager(Const.FileType.Photo);
        getUploadManager(Const.FileType.File);
        getUploadManager(Const.FileType.Video);
    }

    private void getUploadManager(Const.FileType fileType){
        switch (fileType){
            case Photo:
                mPhotoUploadManager = new UploadManager(mContext, APPID, Const.FileType.Photo, PHOTO_BUCKET);
                break;
            case File:
                mFileUploadManager = new UploadManager(mContext, APPID, Const.FileType.File, FILE_BUCKET);
                break;
            case Video:
                mVideoUploadManager = new UploadManager(mContext, APPID, Const.FileType.Video, VIDEO_BUCKET);
                break;
        }

    }

    public boolean upload(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.upload(task);

            case Photo:
                return mPhotoUploadManager.upload(task);

            case Video:
                return mVideoUploadManager.upload(task);
        }

        return false;
    }

    public boolean resume(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.resume(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.resume(task.getTaskId());

            case Video:
                return mVideoUploadManager.resume(task.getTaskId());
        }

        return false;
    }

    public boolean pause(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.pause(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.pause(task.getTaskId());

            case Video:
                return mVideoUploadManager.pause(task.getTaskId());
        }

        return false;
    }

    public boolean cancel(UploadTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.cancel(task.getTaskId());

            case Photo:
                return mPhotoUploadManager.cancel(task.getTaskId());

            case Video:
                return mVideoUploadManager.cancel(task.getTaskId());
        }

        return false;
    }

    public boolean sendCommand(CommandTask task) {
        if (task == null) {
            return false;
        }

        switch (task.getFileType()) {
            case File:
                return mFileUploadManager.sendCommand(task);

            case Photo:
                return mPhotoUploadManager.sendCommand(task);

            case Video:
                return mVideoUploadManager.sendCommand(task);
        }

        return false;
    }

    public void uploadManagerClose(Const.FileType fileType) {
        switch (fileType) {
            case File:
                mFileUploadManager.close();
                break;

            case Photo:
                mPhotoUploadManager.close();
                break;

            case Video:
                mVideoUploadManager.close();
                break;
        }
    }

    public boolean uploadManagerClear(Const.FileType fileType) {
        switch (fileType) {
            case File:
                return mFileUploadManager.clear();

            case Photo:
                return mPhotoUploadManager.clear();

            case Video:
                return mVideoUploadManager.clear();
        }

        return false;
    }

    // 保存签名信息
    public void saveSign_File(QCloudSignBean bean) {
        updateMemorySignInfo(bean, Const.FileType.File);

        if (!TextUtils.isEmpty(APPID))SPUtils.getInstance(mContext).put(SPK_APP_ID, APPID);
        if (!TextUtils.isEmpty(FILE_BUCKET))SPUtils.getInstance(mContext).put(SPK_File_Bucket, FILE_BUCKET);
        if (!TextUtils.isEmpty(FILE_SIGN))SPUtils.getInstance(mContext).put(SPK_File_Sign, FILE_SIGN);
    }// 保存签名信息

    public void saveSign_Photo(QCloudSignBean bean) {
        updateMemorySignInfo(bean, Const.FileType.Photo);

        if (!TextUtils.isEmpty(APPID))SPUtils.getInstance(mContext).put(SPK_APP_ID, APPID);
        if (!TextUtils.isEmpty(PHOTO_BUCKET))SPUtils.getInstance(mContext).put(SPK_Photo_Bucket, PHOTO_BUCKET);
        if (!TextUtils.isEmpty(PHOTO_SIGN))SPUtils.getInstance(mContext).put(SPK_Photo_Sign, PHOTO_SIGN);

    }// 保存签名信息

    public void saveSign_Video(QCloudSignBean bean) {
        updateMemorySignInfo(bean, Const.FileType.Video);

        if (!TextUtils.isEmpty(APPID))SPUtils.getInstance(mContext).put(SPK_APP_ID, APPID);
        if (!TextUtils.isEmpty(PHOTO_BUCKET))SPUtils.getInstance(mContext).put(SPK_Video_Bucket, VIDEO_BUCKET);
        if (!TextUtils.isEmpty(PHOTO_SIGN))SPUtils.getInstance(mContext).put(SPK_Video_Sign, VIDEO_SIGN);
    }

    /**
     * 更新内存中的签名信息
     */
    private void updateMemorySignInfo(QCloudSignBean bean, Const.FileType fileType){
        if (!TextUtils.isEmpty(bean.getAppid())) APPID = bean.getAppid();
        String bucket = bean.getBucketname();
        String sign = bean.getSign();

        switch (fileType){
            case Photo:
                if (!TextUtils.isEmpty(bucket)) PHOTO_BUCKET = bucket;
                if (!TextUtils.isEmpty(sign)) PHOTO_SIGN = sign;
                break;
            case File:
                if (!TextUtils.isEmpty(bucket)) FILE_BUCKET = bucket;
                if (!TextUtils.isEmpty(sign)) FILE_SIGN = sign;
                break;
            case Video:
                if (!TextUtils.isEmpty(bucket)) VIDEO_BUCKET = bucket;
                if (!TextUtils.isEmpty(sign)) VIDEO_SIGN = sign;
                break;
        }

        getUploadManager(Const.FileType.Photo);
        getUploadManager(Const.FileType.File);
        getUploadManager(Const.FileType.Video);

    }

    // 加载签名信息从SharedPreferences到内存
    private void loadSignFromSharedPreferencesToMemory() {
        APPID = SPUtils.getInstance(mContext).get(SPK_APP_ID, "");
        if (TextUtils.isEmpty(APPID)) {
            APPID = DefaultCfg.APPID;
        }

        FILE_BUCKET = SPUtils.getInstance(mContext).get(SPK_File_Bucket, "");
        if (TextUtils.isEmpty(FILE_BUCKET)) {
            FILE_BUCKET = DefaultCfg.FILE_BUCKET;
        }

        PHOTO_BUCKET = SPUtils.getInstance(mContext).get(SPK_Photo_Bucket, "");
        if (TextUtils.isEmpty(PHOTO_BUCKET)) {
            PHOTO_BUCKET = DefaultCfg.PHOTO_BUCKET;
        }

        VIDEO_BUCKET = SPUtils.getInstance(mContext).get(SPK_Video_Bucket, "");
        if (TextUtils.isEmpty(VIDEO_BUCKET)) {
            VIDEO_BUCKET = DefaultCfg.VIDEO_BUCKET;
        }

        FILE_SIGN = SPUtils.getInstance(mContext).get(SPK_File_Sign,"");
        VIDEO_SIGN = SPUtils.getInstance(mContext).get(SPK_Video_Sign,"");
        PHOTO_SIGN = SPUtils.getInstance(mContext).get(SPK_Photo_Sign,"");
    }

}
