package com.driver.aid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Content extends RealmObject implements Parcelable {
    @PrimaryKey
    @Required
    private String id;
    @Required
    private String imageUrl;
    @Required
    private String description;
    @Required
    private String type;
    private String title;

    public static final String sign="sign";
    public static final String hint="hint";

    public Content() {
    }

    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public static String getSign() {
        return sign;
    }

    public static String getHint() {
        return hint;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Content(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        type = in.readString();
        title = in.readString();
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(title);
    }
}
