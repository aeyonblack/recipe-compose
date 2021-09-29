package com.tanya.recipecompose.presentation.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.presentation.navigation.Screen
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListEvent
import com.tanya.recipecompose.util.PAGE_SIZE

@Composable
fun RecipeList(
    loading: Boolean,
    page:Int,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition:(index:Int) -> Unit,
    onTriggerEvent: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    onNavigateToRecipeDetailScreen: (String) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)) {
        LazyColumn {
            itemsIndexed(items = recipes) {index, recipe ->
                onChangeRecipeScrollPosition(index)
                if ((index + 1) >= (page* PAGE_SIZE) && !loading)
                    onTriggerEvent(RecipeListEvent.NextPageEvent)
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        val route = Screen.RecipeDetail.route + "/${recipe.id}"
                        onNavigateToRecipeDetailScreen(route)
                    }
                )
            }
        }
        CircularIndeterminateProgressBar(displayed = loading)
        DefaultSnackbar(
            hostState = scaffoldState.snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onDismiss = {
                scaffoldState
                    .snackbarHostState
                    .currentSnackbarData?.dismiss()
            }
        )
    }
}