package com.example.ggnews

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ggnews.NewsFiles.NewsList
import com.example.ggnews.viewModel.NewViewModel

@Composable
fun MainScreen(newViewModel: NewViewModel = viewModel()) {
    val newsList by newViewModel.getNewsList().observeAsState()

    LaunchedEffect(Unit) {
        newViewModel.refreshData(1)
    }

    when {
        newsList == null -> {
            // 显示加载状态
            Text("加载中...")
        }
        newsList.isNullOrEmpty() -> {
            // 显示空状态
            Text("暂无新闻")
        }
        else -> {
            // 显示新闻列表
            NewsList(news = newsList!!)
        }
    }
}