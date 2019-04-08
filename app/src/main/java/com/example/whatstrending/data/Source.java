package com.example.whatstrending.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(foreignKeys = @ForeignKey(
        entity = Article.class,
        parentColumns = "id",
        childColumns = "article_id",
        onDelete = ForeignKey.CASCADE))
public class Source {

    @ColumnInfo(name = "row_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "article_id")
    private int articleId;

    @SerializedName("id")
    @ColumnInfo(name = "source_id")
    @Expose
    private String sourceId;
    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

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
