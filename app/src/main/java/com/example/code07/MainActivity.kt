package com.example.code07

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.code07.ui.theme.Code07Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Code07Theme {
                YourScreen()
            }
        }
    }
}

@Composable
fun NewsList(news: List<News>) {
    LazyColumn {
        items(news) { item ->
            NewRow(item)
        }
    }
}

// 定义一个函数 NewRow 用于显示单条新闻
@Composable
fun NewRow(new: News) {
    // 创建一个包含填充、宽度、高度的列
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(210.dp)
    ) {
        // 创建一个带有阴影和颜色的提升卡片
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 显示新闻图片，裁剪模式为填满宽度和高度
            Image(
                painter = painterResource(id = new.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            // 显示新闻标题，设置颜色、字体加粗和字体大小
            Text(
                maxLines = 1,
                text = new.titles,
                color = colorResource(id = R.color.myblue),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(2.dp)
            )

            // 显示新闻作者，设置一些填充
            Text(
                maxLines = 1,
                text = new.authors,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

// 定义一个函数 YourScreen 用于显示新闻列表
@Composable
fun YourScreen() {
    // 使用 remember 保存和观察新闻列表状态
    var newsList by remember { mutableStateOf<List<News>>(emptyList()) }
    // 获取当前上下文
    val context = LocalContext.current

    // 使用 LaunchedEffect 初始化新闻列表数据
    LaunchedEffect(Unit) {
        newsList = initData(context)
    }

    // 如果新闻列表不为空，则显示新闻列表
    if (newsList.isNotEmpty()) {
        NewsList(news = newsList)
    }
}

fun initData(context: Context): List<News> {
    val imageStrings: Array<String> = context.resources.getStringArray(R.array.images)
    val titlesArray: Array<String> = context.resources.getStringArray(R.array.titles)
    val authorsArray: Array<String> = context.resources.getStringArray(R.array.authors)

    val newsList = mutableListOf<News>()

    // 确保所有数组长度一致
    val minLength = minOf(imageStrings.size, titlesArray.size, authorsArray.size)

    for (i in 0 until minLength) {
        val news = News()
        news.titles = titlesArray[i]
        news.authors = authorsArray[i]
        val resourceName = imageStrings[i].substringAfterLast("/").substringBeforeLast(".")
        news.image = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        newsList.add(news)
    }

    return newsList
}


// 预览 NewsList
@Preview(showBackground = true)
@Composable
fun NewsListPreview() {
    Code07Theme {
        val sampleNews = listOf(
            News("Title 1", "Author 1", R.drawable.image1),
            News("Title 2", "Author 2", R.drawable.image2),
            News("Title 3", "Author 3", R.drawable.image3)
        )
        NewsList(news = sampleNews)
    }
}
