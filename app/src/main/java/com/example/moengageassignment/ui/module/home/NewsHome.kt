package com.example.moengageassignment.ui.module.home

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.integration.ktx.ExperimentGlideFlows
import com.example.moengageassignment.MainViewModel
import com.example.moengageassignment.R
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.ui.components.AppDropDownMenu
import com.example.moengageassignment.ui.components.DropDownMenuType
import com.example.moengageassignment.ui.theme.appFontFamily
import com.example.moengageassignment.utils.Extensions.openNewsInBrowser
import com.example.moengageassignment.utils.Extensions.toFormattedDate
import com.example.moengageassignment.utils.Extensions.toNewsFooter
import com.example.moengageassignment.utils.Resource
import kotlinx.coroutines.delay

@Composable
fun NewsHome(mainViewModel: MainViewModel) {
    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val newsArticleState by mainViewModel.newsArticle.collectAsState()
            var newsUiVisibleState by remember { mutableStateOf(false) }
            when (newsArticleState) {
                is Resource.Success -> {
                    newsUiVisibleState = true
                }

                is Resource.Loading -> LoadingWidget()

                is Resource.DataError -> ErrorWidget(error = newsArticleState.error.toString()) {
                    mainViewModel.fetchNewsArticles()
                }
            }
            AnimatedVisibility(
                visible = newsUiVisibleState,
                enter = expandVertically(
                    animationSpec = keyframes {
                        this.durationMillis = 500
                    }
                ),

                ) {
                NewsWidget(newsArticleState.data!!) { sortType ->
                    when (sortType) {
                        DropDownMenuType.NewToOld -> {
                            mainViewModel.sortByNewToOld()
                        }

                        DropDownMenuType.OldToNew -> {
                            mainViewModel.sortByOldToNew()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorWidget(error: String, tryAgain: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Try Again!",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .background(color = Color(0xffffc400))
                .padding(16.dp)
                .clickable {
                    tryAgain()
                },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingWidget() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.mango_loading))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            speed = 1.5f,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun NewsWidget(articles: List<NewsArticle>, sortCallback: (DropDownMenuType) -> Unit) {
    var dropDownExpandedState by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
    ) {
        Header()
        Spacer(modifier = Modifier.height(8.dp))
        TopHeadlines(articles)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Just for you")
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Box {
                IconButton(onClick = {
                    dropDownExpandedState = true

                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
                AppDropDownMenu(onClick = {
                    dropDownExpandedState = false
                    sortCallback(it)
                }, expanded = dropDownExpandedState) {
                    dropDownExpandedState = false

                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        NewsArticleList(articles = articles)
    }

}

@Composable
fun Header() {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp)
            )
            Text(
                text = System.currentTimeMillis().toFormattedDate(),
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_mango),
            contentDescription = "AppLogo",
            modifier = Modifier.size(50.dp)
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class,
    ExperimentGlideFlows::class
)
@Composable
fun TopHeadlines(articles: List<NewsArticle>) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight(.4F)
    ) {
        val pagerState = rememberPagerState(pageCount = {
            Int.MAX_VALUE
        })

        //auto loop top headlines
        LaunchedEffect(Unit) {
            while (true) {
                delay(3000L)
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + 1) % 5
                )
            }
        }


        Text(
            text = "Juicy Headlines",
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 18.sp,
                fontFamily = appFontFamily
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            pageSpacing = 16.dp,
        ) { page ->
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.clickable {
                    articles[page].openNewsInBrowser(context)
                }
            ) {
                GlideImage(
                    model = articles[page].urlToImage,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    loading = placeholder(resourceId = R.drawable.ic_placeholder)
                )
                Text(
                    text = articles[page].title.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = appFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
                        .background(color = Color(0xffffc400))
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(.1f)

                )
            }
        }
    }
}


@Composable
fun NewsArticleList(articles: List<NewsArticle>) {
    val context = LocalContext.current
    LazyColumn() {
        items(articles.size) { index ->
            NewsItem(articles[index], context)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(article: NewsArticle, context: Context) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0xfffff9e5))
            .clickable {
                article.openNewsInBrowser(context)
            },
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .animateContentSize()
        ) {
            Text(
                text = article.title.toString(),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp)
                    .copy(
                        lineHeight = 20.sp,
                        fontFamily = appFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                color = Color.Black,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (expanded) {
                Text(
                    text = article.content.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal

                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )

            } else {
                Text(
                    text = article.content.toString(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal

                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = article.toNewsFooter(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Text(
                    text = if (expanded) "Read Less" else "Read More",
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    })
            }

        }
        GlideImage(
            model = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier
                .size(80.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            loading = placeholder(resourceId = R.drawable.ic_placeholder)

        )


    }

}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun previewHomeScreen() {
//    NewsWidget()

}

