package com.example.whatstrending.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

public class ArticleWithSource {
    @Embedded public Article article;

    @Relation(parentColumn = "id", entityColumn = "article_id") public Source source;
}
