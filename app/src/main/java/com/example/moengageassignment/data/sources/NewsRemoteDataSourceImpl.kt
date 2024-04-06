package com.example.moengageassignment.data.sources

import com.example.moengageassignment.data.client.NetworkClient
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.data.dto.NewsSource
import com.example.moengageassignment.utils.Constants
import com.example.moengageassignment.utils.Resource

class NewsRemoteDataSourceImpl : NewsRemoteDataSource {
    override fun getNewsArticles(): Resource<List<NewsArticle>> {
        val articles = mutableListOf<NewsArticle>()
        when (val jsonResponse = NetworkClient.fetchDataFromUrl(Constants.NEWS_URL)) {
            is Resource.Success -> {
                jsonResponse.data?.run {
                    val articlesArray = getJSONArray("articles")
                    for (i in 0 until articlesArray.length()) {
                        val articleObject = articlesArray.getJSONObject(i)
                        val sourceObject = articleObject.getJSONObject("source")
                        val source =
                            NewsSource(
                                sourceObject.optString("id", ""),
                                sourceObject.getString("name")
                            )
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
                    return Resource.Success(articles)
                } ?: return Resource.DataError("null")
            }

            else -> {
                return Resource.DataError(jsonResponse.error)
            }
        }
    }
}