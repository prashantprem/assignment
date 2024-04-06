package com.example.moengageassignment.domain

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.repository.NewsRepository

class GetNewsArticlesUseCase(private val repository: NewsRepository) {
    operator fun invoke(): Result<List<NewsArticle>> {
        return repository.getNewsArticles()
    }
}
