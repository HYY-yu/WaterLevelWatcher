package com.app.feng.waterlevelwatcher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by feng on 2017/4/10.
 */

public class DataBean implements Parcelable {
    public String id;
    public String data1;
    public String data2;
    public String data3;
    public String data4;
    public String data5;
    public String data6;
    public String data7;
    public String data8;

    public DataBean(
            String id,String data1,String data2,String data3,String data4,String data5,String data6,
            String data7,String data8) {
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeString(this.id);
        dest.writeString(this.data1);
        dest.writeString(this.data2);
        dest.writeString(this.data3);
        dest.writeString(this.data4);
        dest.writeString(this.data5);
        dest.writeString(this.data6);
        dest.writeString(this.data7);
        dest.writeString(this.data8);
    }

    protected DataBean(Parcel in) {
        this.id = in.readString();
        this.data1 = in.readString();
        this.data2 = in.readString();
        this.data3 = in.readString();
        this.data4 = in.readString();
        this.data5 = in.readString();
        this.data6 = in.readString();
        this.data7 = in.readString();
        this.data8 = in.readString();
    }

    public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel source) {
            return new DataBean(source);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };
}
