package com.example.moengageassignment

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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


}