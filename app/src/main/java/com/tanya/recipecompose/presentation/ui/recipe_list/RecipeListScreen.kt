package com.tanya.recipecompose.presentation.ui.recipe_list

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.tanya.recipecompose.presentation.components.RecipeList
import com.tanya.recipecompose.presentation.components.SearchBar
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListEvent.NewSearchEvent
import com.tanya.recipecompose.presentation.ui.theme.RecipeComposeTheme

@Composable
fun RecipeListScreen(
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    viewModel: RecipeListViewModel
) {

    // mutable data
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val loading = viewModel.loading.value
    val page = viewModel.page.value

    // focus manager
    val focusManager = LocalFocusManager.current

    // scaffold state
    val scaffoldState = rememberScaffoldState()

    RecipeComposeTheme(darkTheme = false) {
        Scaffold(
            topBar = {
                SearchBar(
                    query = query,
                    selectedCategory = selectedCategory,
                    categoryScrollPosition = viewModel.categoryScrollPosition,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(NewSearchEvent)
                    },
                    onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                    onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                    focusManager = focusManager,
                    onToggleTheme = {} /*app::toggleTheme*/
                )
            },
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState }
        ) {
            RecipeList(
                loading = loading,
                page = page,
                recipes = recipes,
                onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                onTriggerEvent = viewModel::onTriggerEvent,
                scaffoldState = scaffoldState,
                onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen
            )
        }
    }
}