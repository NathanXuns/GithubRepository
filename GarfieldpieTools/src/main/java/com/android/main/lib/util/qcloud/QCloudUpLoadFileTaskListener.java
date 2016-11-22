package com.android.main.lib.util.qcloud;

import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;

public class QCloudUpLoadFileTaskListener implements IUploadTaskListener {

    private int failNum; // 上传失败文件数量
    private int successNum; // 上传成功文件数量
    private int totalNum; // 上传总文件数量
    private int currentIndex; // 当前上传游标


    private UploadFileListener listener;

    public QCloudUpLoadFileTaskListener(int totalNum, UploadFileListener listener) {
        this.totalNum = totalNum;
        this.failNum = 0;
        this.successNum = 0;
        this.currentIndex = 0;
        this.listener = listener;
    }

    @Override
    public void onUploadSucceed(FileInfo fileInfo) {
        successNum = successNum + 1;
        checkUploadStatue();
    }

    @Override
    public void onUploadFailed(int i, String s) {
        failNum = failNum + 1;
        checkUploadStatue();
    }

    @Override
    public void onUploadProgress(long l, long l1) {
        currentIndex = currentIndex + 1;
        if (null != listener) {
            listener.onUploadProgress(l,l1);
        }
    }

    @Override
    public void onUploadStateChange(ITask.TaskState taskState) {
        if (null != listener) {
            listener.onUploadStateChange(taskState);
        }
    }

    private void checkUploadStatue(){
        if (failNum + successNum == totalNum && listener != null) {
            if (totalNum == successNum) {
                listener.onUploadSucceed();
            }else{
                listener.onUploadFailed(totalNum,failNum);
            }
        }
    }

    public UploadFileListener getListener() {
        return listener;
    }

    public void setTotalNum(int totalNum){
        this.totalNum = totalNum;
    }

    public interface UploadFileListener {
        void onUploadSucceed();

        void onUploadFailed(int totalNum, int failNUm);

        void onUploadProgress(long var1, long var3);

        void onUploadStateChange(ITask.TaskState var1);
    }
}
