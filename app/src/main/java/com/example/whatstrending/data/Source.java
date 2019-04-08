package com.example.whatstrending.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Source {

    private int id;

    @SerializedName("id")
    @ColumnInfo(name = "source_id")
    @Expose
    private Object sourceId;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getSourceId() {
        return sourceId;
    }

    public void setSourceId(Object sourceId) {
        this.sourceId = sourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
