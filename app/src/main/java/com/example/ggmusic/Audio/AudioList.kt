package com.example.ggmusic.Audio

import AudioItem
import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ggmusic.MainActivity
import com.example.ggmusic.MusicService
import com.example.ggmusic.Player.Player
import com.example.ggmusic.Player.PlayerViewModel
import com.example.ggmusic.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioList(audioFiles: List<AudioFile>,viewModel: PlayerViewModel) {
    var sliderPosition by remember { mutableStateOf(0f) }
    var currentMusic by remember { mutableStateOf(AudioFile()) }
    var player by remember {
        mutableStateOf(Player())
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(128, 0, 128),  // 紫色
                    contentColor = Color(128, 0, 128)     // 紫色
                ),
                shape = MaterialTheme.shapes.small.copy(CornerSize(0))
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(text = "GG音乐播放器", fontSize = 28.sp, color = Color.White)
                }
            }
        },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp),
            ) {
                Column {
                    viewModel.mService?.getCurrentPosition()?.let {
                        Slider(
                            value = it.toFloat(),
                            onValueChange = { value ->
                                viewModel.mService!!.seekTo(value.toInt())
                            },
                            modifier = Modifier.height(12.dp),
                            valueRange = 0f..currentMusic.duration.toFloat()
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,//垂直居中
                        horizontalArrangement = Arrangement.SpaceBetween,//水平间距
                        modifier = Modifier.fillMaxWidth()//填充父布局
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_album_24),
                                contentDescription = null
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = currentMusic.title,
                                    fontSize = 20.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.width(180.dp)
                                )
                                Text(
                                    text = currentMusic.artist,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.width(150.dp)
                                )

                            }
                        }

                        IconButton(onClick = {
                            if (viewModel.mService?.isPlaying() == true) {
                                viewModel.mService?.pause()

                            } else {
                                viewModel.mService?.resume()

                            }
                        }) {
                            Image(
                                painter = painterResource(id = if (viewModel.mService?.isPlaying() == true) R.drawable.baseline_album_24 else R.drawable.baseline_ads_click_24),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        },

        ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            itemsIndexed(audioFiles) { index, audio ->
                AudioItem(index,
                    audio.title,
                    audio.artist,
                    audio.filePath,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                        onClick = {
                            currentMusic = audio
                            val serviceIntent = Intent(context, MusicService::class.java)
                            serviceIntent.putExtra(MainActivity.URL, currentMusic.filePath)
                            serviceIntent.putExtra(MainActivity.TITLE, currentMusic.title)
                            serviceIntent.putExtra(MainActivity.ARTIST, currentMusic.artist)
                            context.startService(serviceIntent)
                        }
                    )
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AudioListPreview() {
    val audioFiles = listOf(
        AudioFile(1, "Artist 1", "path/to/file1"),
        AudioFile(2, "Artist 2", "path/to/file2"),
        AudioFile(3, "Artist 3", "path/to/file3")
    )

    val viewModel = PlayerViewModel()

    AudioList(audioFiles = audioFiles, viewModel = viewModel)
}
