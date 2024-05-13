package com.github.zottaa.com.github.zottaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.news_main.NewsMainScreen
import com.github.news.uikit.NewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsTheme {
                NewsMainScreen()
            }
        }
    }
}