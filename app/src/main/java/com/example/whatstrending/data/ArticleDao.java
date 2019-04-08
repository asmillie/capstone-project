package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY publishedAt")
    LiveData<List<Article>> getAllArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveArticles(List<Article> articles);

    @Delete
    void deleteArticles(List<Article> articles);
}
