package com.example.moengageassignment.domain

import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.repository.NewsRepository
import com.example.moengageassignment.utils.Resource

class GetNewsArticlesUseCase(private val repository: NewsRepository) {
    operator fun invoke(): Resource<List<NewsArticle>> {
        return repository.getNewsArticles()
    }
}
