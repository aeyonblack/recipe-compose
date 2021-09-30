package com.tanya.recipecompose.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.interactors.recipe.GetRecipe
import com.tanya.recipecompose.presentation.ui.recipe.RecipeEvent.GetRecipeEvent
import com.tanya.recipecompose.presentation.util.ConnectivityManager
import com.tanya.recipecompose.util.STATE_KEY_RECIPE
import com.tanya.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    private val getRecipe: GetRecipe,
    private val connectivityManager: ConnectivityManager,
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

    private fun getRecipe(id:Int) {
        getRecipe.execute(
            recipeId = id,
            token = token,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach {
            loading.value = it.loading
            it.data?.let { recipe ->
                this.recipe.value = recipe
                state.set(STATE_KEY_RECIPE, recipe.id)
            }
            it.error?.let { e ->
                Log.e(TAG, "getRecipe:$e")
            }
        }.launchIn(viewModelScope)
    }
}