package com.example.code07

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.code07.ui.theme.Code07Theme
import com.example.code07.DB.*
import androidx.compose.runtime.livedata.observeAsState
// 主界面的创建
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Code07Theme {
                MainScreen()
            }
        }
    }
}
// 主屏幕的Composable函数
@Composable
fun MainScreen (){
    // 获取当前的Context和ViewModel
    val context =   LocalContext.current
    val newViewModel = NewViewModel(NewRepository(context))
    // 观察ViewModel中的数据变化
    val newsList by newViewModel.getNewsList().observeAsState()
    // 在LaunchedEffect中进行数据的初始化和获取
    LaunchedEffect(Unit) {
        newViewModel.delData()
        // 从资源文件中获取数据
        val imageStrings: Array<String>  = context.resources.getStringArray(R.array.images)
        val titlesArray: Array<String> = context.resources.getStringArray(R.array.titles)
        val authorsArray:Array<String> =context.resources.getStringArray(R.array.authors)
        // 将获取的数据插入到数据库中
        for (i in titlesArray.indices) {
            val new = New()
            new.titles = titlesArray[i]
            new.authors = authorsArray[i]
            val resourceName = imageStrings[i].substringAfterLast("/").substringBeforeLast(".")
            new.image=context.resources.getIdentifier(resourceName, "drawable", context.packageName)
            newViewModel.insert(new)
        }
        newViewModel.getAll()
    }
    // 显示新闻列表
    newsList?.let { NewsList(news = it) }

}

// 新闻列表的Composable函数
@Composable
fun NewsList(news: List<New>) {
    LazyColumn {
        items(news) { item ->
            NewRow(item)
        }
    }
}

// 新闻行的Composable函数
@Composable
fun NewRow(new: New) {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(210.dp)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = new.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Text(
                maxLines = 1,
                text = new.titles,
                color = colorResource(id = R.color.myblue),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(2.dp)
            )

            Text(
                maxLines = 1,
                text = new.authors,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}
