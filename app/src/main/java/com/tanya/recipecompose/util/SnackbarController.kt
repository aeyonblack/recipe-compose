package com.tanya.recipecompose.util

import androidx.compose.material.ScaffoldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SnackbarController(private val scope: CoroutineScope) {

    private var snackbarJob: Job? = null

    fun getScope() = scope

    init {
        cancelActiveJob()
    }

    fun showSnackbar(
        scaffoldState: ScaffoldState,
        message: String,
        label: String,
    ) {
        if (snackbarJob == null) {
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = label
                )
                cancelActiveJob()
            }

        } else {
            cancelActiveJob()
            snackbarJob = scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = label
                )
                cancelActiveJob()
            }
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}