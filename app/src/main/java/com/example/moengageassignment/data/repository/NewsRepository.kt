package com.example.moengageassignment.data.repository

import com.example.moengageassignment.data.dto.NewsArticle

interface NewsRepository {
    fun getNewsArticles(): List<NewsArticle>
}
