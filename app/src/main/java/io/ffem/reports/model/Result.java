package io.ffem.reports.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable {

    @SuppressWarnings("unused")
    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("unit")
    @Expose
    private final String unit;
    private final String result;
    private final Double resultValue;

    private Result(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        unit = in.readString();
        result = in.readString();
        resultValue = in.readByte() == 0x00 ? null : in.readDouble();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeString(result);
        if (resultValue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(resultValue);
        }
    }

    public String getResult() {
        return result;
    }

}