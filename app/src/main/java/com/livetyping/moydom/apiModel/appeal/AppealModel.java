package com.livetyping.moydom.apiModel.appeal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ivan on 13.12.2017.
 */

public class AppealModel implements Parcelable {
    private int id;
    private String typeName;
    private String name;
    private String email;

    public AppealModel() {
    }

    protected AppealModel(Parcel in) {
        id = in.readInt();
        typeName = in.readString();
        name = in.readString();
        email = in.readString();
    }

    public static final Creator<AppealModel> CREATOR = new Creator<AppealModel>() {
        @Override
        public AppealModel createFromParcel(Parcel in) {
            return new AppealModel(in);
        }

        @Override
        public AppealModel[] newArray(int size) {
            return new AppealModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(typeName);
        parcel.writeString(name);
        parcel.writeString(email);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppealModel){
            if (id == ((AppealModel) obj).getId()
                    && typeName.equals(((AppealModel) obj).getTypeName())
                    && name.equals(((AppealModel) obj).getName())
                    && email.equals(((AppealModel) obj).getEmail())){
                return true;
            }
        }
        return false;
    }
}
