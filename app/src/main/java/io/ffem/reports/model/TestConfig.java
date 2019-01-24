package io.ffem.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestConfig {

    @SerializedName("tests")
    @Expose
    private final List<TestInfo> tests = null;

    public List<TestInfo> getTests() {
        return tests;
    }
}
