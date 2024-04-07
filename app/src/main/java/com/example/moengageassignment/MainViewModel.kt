package com.example.moengageassignment

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.domain.GetNewsArticlesUseCase
import com.example.moengageassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNewsArticlesUseCase: GetNewsArticlesUseCase
) : ViewModel() {
    var showSplash by mutableStateOf(true)
    private val _newsArticles: MutableStateFlow<Resource<List<NewsArticle>>> =
        MutableStateFlow(Resource.Loading())
    val newsArticle: StateFlow<Resource<List<NewsArticle>>> = _newsArticles.asStateFlow()

    init {
        fetchNewsArticles()
    }


    private fun fetchNewsArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            _newsArticles.emit(Resource.Loading())
            when (val res = getNewsArticlesUseCase()) {
                is Resource.Success -> {
                    _newsArticles.value = Resource.Success(res.data!!)
                    Log.d("NewsResponseTest", res.data.toString())
                }

                is Resource.DataError -> {
                    _newsArticles.value = Resource.DataError(res.error!!)
                    Log.d("NewsResponseTest", res.error.toString())
                }

                else -> {}

            }
        }
    }

    fun sortByNewToOld() {
        val currentList = _newsArticles.value.data ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val sortedList = sortByNewToOld(currentList)
            _newsArticles.value = Resource.Success(sortedList)
        }

    }

    fun sortByOldToNew() {
        val currentList = _newsArticles.value.data ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val sortedList = sortByOldToNew(currentList)
            _newsArticles.value = Resource.Success(sortedList)
        }
    }

    private fun sortByNewToOld(articles: List<NewsArticle>): List<NewsArticle> {
        return articles.sortedByDescending {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            dateFormat.parse(it.publishedAt ?: "") ?: dateFormat.parse("1970-01-01T00:00:00Z")
        }
    }

    private fun sortByOldToNew(articles: List<NewsArticle>): List<NewsArticle> {
        return articles.sortedBy {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            dateFormat.parse(it.publishedAt ?: "") ?: dateFormat.parse("1970-01-01T00:00:00Z")
        }
    }


}