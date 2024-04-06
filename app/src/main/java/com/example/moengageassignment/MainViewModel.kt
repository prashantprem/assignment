package com.example.moengageassignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.domain.GetNewsArticlesUseCase
import com.example.moengageassignment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNewsArticlesUseCase: GetNewsArticlesUseCase
) : ViewModel() {
    var showSplash by mutableStateOf(true)
    private val _newsArticles = MutableLiveData<Resource<List<NewsArticle>>>()
    val newsArticles: LiveData<Resource<List<NewsArticle>>> get() = _newsArticles

    init {
        fetchNewsArticles()
    }


    private fun fetchNewsArticles() {
        viewModelScope.launch {
            _newsArticles.postValue(Resource.Loading())
            when (val res = getNewsArticlesUseCase()) {
                is Resource.Success -> {
                    _newsArticles.postValue(Resource.Success(res.data!!))
                }

                is Resource.DataError -> {
                    _newsArticles.postValue(Resource.DataError(res.error!!))
                }

                else -> {}
            }
        }
    }


}