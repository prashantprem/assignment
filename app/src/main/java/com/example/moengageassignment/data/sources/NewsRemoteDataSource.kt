package com.example.moengageassignment.data.sources

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.utils.Resource

interface NewsRemoteDataSource {
    fun getNewsArticles(): Resource<List<NewsArticle>>
}