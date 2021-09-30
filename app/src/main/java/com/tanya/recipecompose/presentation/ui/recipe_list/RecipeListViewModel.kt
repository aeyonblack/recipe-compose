package com.tanya.recipecompose.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.interactors.recipe_list.RestoreRecipes
import com.tanya.recipecompose.interactors.recipe_list.SearchRecipes
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListEvent.*
import com.tanya.recipecompose.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val searchRecipes: SearchRecipes,
    private val restoreRecipes: RestoreRecipes,
    @Named("token") private val token: String,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())

    val query = mutableStateOf("")

    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)

    var categoryScrollPosition: Int = 0

    val loading = mutableStateOf(false)

    val page = mutableStateOf(1)

    private var recipeListScrollPosition = 0

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p -> setPage(p) }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q -> setQuery(q) }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { pos ->
            setListScrollPosition(pos)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if (recipeListScrollPosition != 0) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(NewSearchEvent)
        }
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                    is RestoreStateEvent -> {
                        restoreState()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: $e, ${e.cause}")
            }
        }
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    private fun newSearch() {
        resetSearchState()
        searchRecipes.execute(
            token = token,
            page = page.value,
            query = query.value
        ).onEach {
            loading.value = it.loading
            it.data?.let { list ->
                recipes.value = list
            }
            it.error?.let { e ->
                Log.e(TAG, "newSearch:$e")
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((recipeListScrollPosition + 1) >= (page.value* PAGE_SIZE)) {
            loading.value = true
            incrementPage()

            if (page.value > 1) {
                searchRecipes.execute(
                    token = token,
                    page = page.value,
                    query = query.value
                ).onEach {
                    loading.value = it.loading
                    it.data?.let { list ->
                        appendRecipes(list)
                    }
                    it.error?.let {e ->
                        Log.e(TAG, "nextPage:$e")
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun restoreState() {
        restoreRecipes.execute(
            page = page.value,
            query = query.value
        ).onEach {
            loading.value = it.loading
            it.data?.let { list ->
                recipes.value = list
            }
            it.error?.let {e ->
                Log.e(TAG, "restoreState:$e")
            }
        }.launchIn(viewModelScope)
    }

    fun onSelectedCategoryChange(category:String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Int) {
        categoryScrollPosition = position
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory.value?.value != query.value)
            clearSelectedCategory()
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position = position)
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }

}