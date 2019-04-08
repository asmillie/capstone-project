package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ArticleWithSourceDao {
    @Query("SELECT * FROM articles LEFT JOIN sources ON articles.id = sources.article_id")
    LiveData<List<ArticleWithSource>> getAllArticlesWithSource();
}
