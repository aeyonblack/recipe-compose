package com.tanya.recipecompose.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import com.tanya.recipecompose.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    repository: RecipeRepository,
    @Named("token") token: String
): ViewModel() {
    init {
        println("ViewModel: $repository")
        println("ViewModel: $token")
    }
}