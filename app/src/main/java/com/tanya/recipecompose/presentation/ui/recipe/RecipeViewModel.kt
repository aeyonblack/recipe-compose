package com.tanya.recipecompose.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.presentation.ui.recipe.RecipeEvent.*
import com.tanya.recipecompose.repository.RecipeRepository
import com.tanya.recipecompose.util.STATE_KEY_RECIPE
import com.tanya.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    @Named("token") private val token: String,
    private val state: SavedStateHandle
): ViewModel() {

    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    val onLoad = mutableStateOf(false)

    init {
        state.get<Int>(STATE_KEY_RECIPE)?.let { onTriggerEvent(GetRecipeEvent(it)) }
    }

    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetRecipeEvent -> event.id?.let { getRecipe(it) }
                }
            } catch (e:Exception) {
                Log.e(TAG, "onTriggerEvent, Exception:$e, ${e.cause} ")
            }
        }
    }

    private suspend fun getRecipe(id:Int) {
        loading.value = true
        delay(1000)
        val recipe = repository.get(token, id)
        this.recipe.value = recipe
        state.set(STATE_KEY_RECIPE, recipe.id)
        loading.value = false
    }
}