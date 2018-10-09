package com.gravity.customprogressbar;

/**
 * Created by Akash Verma on 21/04/18.
 */

public class CustomProgressModel {

    private int sectionCount;
    private String labelName;

    public CustomProgressModel(int sectionCount, String labelName) {
        this.sectionCount = sectionCount;
        this.labelName = labelName;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public void setSectionCount(int sectionCount) {
        this.sectionCount = sectionCount;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
