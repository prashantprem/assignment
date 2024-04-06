package com.example.moengageassignment.ui.module.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moengageassignment.MainViewModel

@Composable
fun NewsHome(mainViewModel: MainViewModel) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun previewHomeScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    NewsHome(mainViewModel = viewModel)

}

