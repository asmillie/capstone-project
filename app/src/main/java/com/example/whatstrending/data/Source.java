package com.example.whatstrending.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {

    @SerializedName("id")
    @Expose
    private String sourceId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
