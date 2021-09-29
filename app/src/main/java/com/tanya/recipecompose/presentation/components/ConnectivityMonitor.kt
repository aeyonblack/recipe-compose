package com.tanya.recipecompose.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * When a network connection cannot be established
 * this composable displays a message to alert the user
 */
@Composable
fun ConnectivityMonitor(isNetworkAvailable:Boolean) {
    if (!isNetworkAvailable) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "You're not connected",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}