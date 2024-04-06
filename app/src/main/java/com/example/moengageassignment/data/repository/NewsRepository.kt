package com.example.moengageassignment.data.repository

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.utils.Resource

interface NewsRepository {
    fun getNewsArticles(): Resource<List<NewsArticle>>
}
