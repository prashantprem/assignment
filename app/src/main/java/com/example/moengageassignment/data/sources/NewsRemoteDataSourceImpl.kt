package com.example.moengageassignment.data.sources

import com.example.moengageassignment.data.client.NetworkClient
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.dto.NewsSource
import com.example.moengageassignment.utils.Constants

class NewsRemoteDataSourceImpl : NewsRemoteDataSource {
    override fun getNewsArticles(): Result<List<NewsArticle>> {
        val articles = mutableListOf<NewsArticle>()
        val jsonResponse = NetworkClient.fetchDataFromUrl(Constants.NEWS_URL)
        if (jsonResponse.isFailure) {
            return Result.failure(Error(jsonResponse.exceptionOrNull()))
        } else {
            jsonResponse.getOrNull()?.run {
                val articlesArray = getJSONArray("articles")
                for (i in 0 until articlesArray.length()) {
                    val articleObject = articlesArray.getJSONObject(i)
                    val sourceObject = articleObject.getJSONObject("source")
                    val source =
                        NewsSource(sourceObject.optString("id", ""), sourceObject.getString("name"))
                    val article = NewsArticle(
                        source = source,
                        author = articleObject.optString("author", null),
                        title = articleObject.getString("title"),
                        description = articleObject.optString("description", null),
                        url = articleObject.getString("url"),
                        urlToImage = articleObject.optString("urlToImage", null),
                        publishedAt = articleObject.getString("publishedAt"),
                        content = articleObject.optString("content", null)
                    )
                    articles.add(article)
                }
                return Result.success(articles)
            } ?: return Result.failure(Error(jsonResponse.exceptionOrNull()))
        }
    }
}