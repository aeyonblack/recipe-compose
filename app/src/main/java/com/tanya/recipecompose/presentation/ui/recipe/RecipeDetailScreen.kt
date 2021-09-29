package com.tanya.recipecompose.presentation.ui.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tanya.recipecompose.presentation.components.CircularIndeterminateProgressBar
import com.tanya.recipecompose.presentation.components.RecipeView
import com.tanya.recipecompose.presentation.ui.theme.RecipeComposeTheme

@Composable
fun RecipeDetailScreen(
    recipeId: Int?,
    viewModel: RecipeViewModel
) {
    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))
    }

    val recipe = viewModel.recipe.value
    val loading = viewModel.loading.value
    val scaffoldState = rememberScaffoldState()

    RecipeComposeTheme(darkTheme = false) {
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (loading && recipe == null)
                    Text(text = "Loading...")
                else {
                    recipe?.let {
                        RecipeView(recipe = it)
                    }
                }
                CircularIndeterminateProgressBar(displayed = loading)
            }
        }
    }
}