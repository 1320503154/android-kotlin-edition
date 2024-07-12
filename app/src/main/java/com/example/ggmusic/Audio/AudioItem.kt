package com.example.ggmusic.Audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioItem(
    index: Int,
    title: String,
    artist: String,
    filePath: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = (index + 1).toString(), fontSize = 18.sp)
            Spacer(
                modifier = Modifier
                    .width(25.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = title, fontSize = 18.sp)
                Text(text = artist, fontSize = 14.sp)
                Spacer(
                    modifier = Modifier
                        .height(0.5.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun AudioItemPreView() {
    MaterialTheme {
        AudioItem(index = 2, title = "阿发阿帆", artist = "asd", filePath = "xxxxx")
    }
}
