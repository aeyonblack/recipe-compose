package com.tanya.recipecompose.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Used as a placeholder when the requested data
 * is not available
 */
@Composable
fun NothingHere() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text =  "¯\\_(ツ)_/¯",
                style = TextStyle(fontSize = 55.sp)
            )
            Text(
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                text = "There is nothing here",
                style = MaterialTheme.typography.h4
            )
        }
    }
}