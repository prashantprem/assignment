package com.example.moengageassignment.ui.module.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moengageassignment.MainViewModel
import com.example.moengageassignment.utils.Resource

@Composable
fun NewsHome(mainViewModel: MainViewModel) {
    Scaffold {
        val newsArticleState by mainViewModel.newsArticle.collectAsState()
        when (newsArticleState) {
            is Resource.Success -> {

            }

            is Resource.Loading -> LoadingWidget()

            is Resource.DataError -> {}
        }
        Column(modifier = Modifier.padding(it)) {

        }
    }
}

@Composable
fun LoadingWidget() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun NewsWidget() {

}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun previewHomeScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    NewsHome(mainViewModel = viewModel)

}

