package com.tanya.recipecompose.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tanya.recipecompose.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
    }
}
