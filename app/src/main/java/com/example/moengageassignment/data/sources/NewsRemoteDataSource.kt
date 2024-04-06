package com.example.moengageassignment.data.sources

import com.example.moengageassignment.data.dto.NewsArticle

interface NewsRemoteDataSource {
    fun getNewsArticles(): Result<List<NewsArticle>>
}