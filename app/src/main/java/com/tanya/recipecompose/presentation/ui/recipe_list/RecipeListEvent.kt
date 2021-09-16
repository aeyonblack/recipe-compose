package com.tanya.recipecompose.presentation.ui.recipe_list

sealed class RecipeListEvent {
    object NewSearchEvent: RecipeListEvent()
    object NextPageEvent: RecipeListEvent()
    object RestoreStateEvent: RecipeListEvent()
}