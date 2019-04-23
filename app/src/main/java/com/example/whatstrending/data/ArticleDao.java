package com.example.whatstrending.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.whatstrending.Constants;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT id, title, url_to_image FROM articles ORDER BY published_at")
    LiveData<List<Article>> getAllHeadlines();

    @Query("SELECT id FROM articles ORDER BY published_at")
    LiveData<List<Article>> getAllArticleIds();

    @Query("SELECT * FROM articles WHERE id = :id")
    LiveData<Article> getArticleById(int id);

    @Query("SELECT * FROM articles ORDER BY published_at LIMIT :limit")
    List<Article> getTopHeadlines(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveArticles(List<Article> articles);

    @Query("DELETE FROM articles")
    void deleteAllArticles();
}
