package com.example.moengageassignment.data.repository

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.sources.NewsRemoteDataSource

class NewsRepositoryImpl(private val remoteDataSource: NewsRemoteDataSource) : NewsRepository {
    override fun getNewsArticles(): List<NewsArticle> {
        return remoteDataSource.getNewsArticles()
    }
}
