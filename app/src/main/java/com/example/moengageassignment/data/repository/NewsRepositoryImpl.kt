package com.example.moengageassignment.data.repository

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.sources.NewsRemoteDataSource
import com.example.moengageassignment.utils.Resource

class NewsRepositoryImpl(private val remoteDataSource: NewsRemoteDataSource) : NewsRepository {
    override fun getNewsArticles(): Resource<List<NewsArticle>>{
        return remoteDataSource.getNewsArticles()
    }
}
