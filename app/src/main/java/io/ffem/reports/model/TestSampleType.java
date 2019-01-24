package io.ffem.reports.model;

import com.google.gson.annotations.SerializedName;

public enum TestSampleType {
    @SerializedName("all")
    ALL,

    @SerializedName("soil")
    SOIL,

    @SerializedName("water")
    WATER
}

