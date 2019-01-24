package io.ffem.reports.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class TestInfo implements Parcelable {

    public static final Creator<TestInfo> CREATOR = new Creator<TestInfo>() {
        @Override
        public TestInfo createFromParcel(Parcel in) {
            return new TestInfo(in);
        }

        @Override
        public TestInfo[] newArray(int size) {
            return new TestInfo[size];
        }
    };
    @SerializedName("isCategory")
    @Expose
    private boolean isCategory;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private TestSampleType sampleType;
    @SerializedName("subtype")
    @Expose
    private TestType subtype;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("calibration")
    @Expose
    private String calibration;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("brandUrl")
    @Expose
    private String brandUrl;
    @SerializedName("illuminant")
    @Expose
    private String illuminant;
    @SerializedName("length")
    @Expose
    private Double length;
    @SerializedName("height")
    @Expose
    private Double height;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("results")
    @Expose
    private List<Result> results;
    @SerializedName("ranges")
    @Expose
    private String ranges;
    @SerializedName("defaultColors")
    @Expose
    private String defaultColors;
    @SerializedName("hueTrend")
    @Expose
    private Integer hueTrend;
    @SerializedName("dilutions")
    @Expose
    private List<Integer> dilutions = new ArrayList<>();
    @SerializedName("monthsValid")
    @Expose
    private Integer monthsValid;
    @SerializedName("sampleQuantity")
    @Expose
    private String sampleQuantity;
    @SerializedName("selectInstruction")
    @Expose
    private String selectInstruction;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("numPatch")
    @Expose
    private Integer numPatch;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("responseFormat")
    @Expose
    private String responseFormat;
    @SerializedName("imageScale")
    @Expose
    private String imageScale;
    private String resultSuffix = "";

    private Integer decimalPlaces;

    private TestInfo(Parcel in) {
        isCategory = in.readByte() != 0;
        category = in.readString();
        name = in.readString();
        subtype = TestType.valueOf(in.readString());
        sampleType = TestSampleType.valueOf(in.readString());
        description = in.readString();
        tags = in.createStringArrayList();
        uuid = in.readString();
        calibration = in.readString();
        brand = in.readString();
        brandUrl = in.readString();
        unit = in.readString();
        ranges = in.readString();
        sampleQuantity = in.readString();
        results = new ArrayList<>();
        in.readTypedList(results, Result.CREATOR);
        selectInstruction = in.readString();
        image = in.readString();
        responseFormat = in.readString();
        if (in.readByte() == 0) {
            decimalPlaces = 0;
        } else {
            decimalPlaces = in.readInt();
        }
        resultSuffix = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isCategory ? 1 : 0));
        parcel.writeString(category);
        parcel.writeString(name);
        parcel.writeString(subtype.name());
        parcel.writeString(sampleType.name());
        parcel.writeString(description);
        parcel.writeStringList(tags);
        parcel.writeString(uuid);
        parcel.writeString(calibration);
        parcel.writeString(brand);
        parcel.writeString(brandUrl);
        parcel.writeString(unit);
        parcel.writeString(ranges);
        parcel.writeString(sampleQuantity);
        parcel.writeTypedList(results);
        parcel.writeString(selectInstruction);
        parcel.writeString(image);
        parcel.writeString(responseFormat);
        if (decimalPlaces == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(decimalPlaces);
        }
        parcel.writeString(resultSuffix);
    }

    public List<Result> getResults() {
        return results;
    }

    public String getResultSuffix() {
        if (resultSuffix != null) {
            return resultSuffix;
        } else {
            return "";
        }
    }

    public void setResultSuffix(String resultSuffix) {
        if (resultSuffix != null) {
            this.resultSuffix = resultSuffix;
        }
    }
}