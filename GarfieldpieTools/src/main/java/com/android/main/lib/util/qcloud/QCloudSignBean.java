package com.android.main.lib.util.qcloud;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 万象优图签名Bean
 */

public class QCloudSignBean implements Parcelable {

    /**
     * sign : zEvyWeZ4swmoTpUvC4RwCQnZ3TxhPTEwMDU0MzM3JmI9cmVzaXplJms9QUtJREVYMEhmcEVqSDVOYzZVcXBMOFphNFJFVU9aOXhrcmJ2JmU9MTQ3NzM5MjA3NSZ0PTE0NzQ4MDAwNzUmcj0xNDc0MzExMTAzJnU9MCZmPQ==
     * appid : 10054337
     * bucketname : resize
     */

    private String sign;
    private String appid;
    private String bucketname;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBucketname() {
        return bucketname;
    }

    public void setBucketname(String bucketname) {
        this.bucketname = bucketname;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sign);
        dest.writeString(this.appid);
        dest.writeString(this.bucketname);
    }

    public QCloudSignBean() {
    }

    protected QCloudSignBean(Parcel in) {
        this.sign = in.readString();
        this.appid = in.readString();
        this.bucketname = in.readString();
    }

    public static final Parcelable.Creator<QCloudSignBean> CREATOR = new Parcelable.Creator<QCloudSignBean>() {
        @Override
        public QCloudSignBean createFromParcel(Parcel source) {
            return new QCloudSignBean(source);
        }

        @Override
        public QCloudSignBean[] newArray(int size) {
            return new QCloudSignBean[size];
        }
    };
}
