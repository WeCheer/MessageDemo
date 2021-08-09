package com.wyc.message.msg;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： wyc
 * <p>
 * 创建时间： 2020/4/15 20:23
 * <p>
 * 文件名字： com.wyc.vivodemo.msg
 * <p>
 * 类的介绍：
 */
public class MessageEntity implements Parcelable {

    private String time;
    private String name;

    public MessageEntity() {
    }

    public MessageEntity(String time, String name) {
        this.time = time;
        this.name = name;
    }

    protected MessageEntity(Parcel in) {
        time = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageEntity> CREATOR = new Creator<MessageEntity>() {
        @Override
        public MessageEntity createFromParcel(Parcel in) {
            return new MessageEntity(in);
        }

        @Override
        public MessageEntity[] newArray(int size) {
            return new MessageEntity[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "time='" + time + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
