package com.tanya.recipecompose.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tanya.recipecompose.presentation.components.CircularIndeterminateProgressBar
import com.tanya.recipecompose.presentation.components.FoodCategoryMenu
import com.tanya.recipecompose.presentation.components.RecipeCard
import com.tanya.recipecompose.presentation.components.SearchBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("recipeListFragment: $viewModel")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply { 
            setContent {

                // mutable data
                val recipes = viewModel.recipes.value
                val query = viewModel.query.value
                val selectedCategory = viewModel.selectedCategory.value
                val loading = viewModel.loading.value

                // focus manager
                val focusManager = LocalFocusManager.current

                Column {

                    // the search bar for searching recipes
                    SearchBar(
                        query = query,
                        selectedCategory = selectedCategory,
                        categoryScrollPosition = viewModel.categoryScrollPosition,
                        onQueryChanged = viewModel::onQueryChanged,
                        onExecuteSearch = viewModel::newSearch,
                        onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                        onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                        focusManager = focusManager
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn {
                            itemsIndexed(items = recipes) {index, recipe ->
                                RecipeCard(recipe = recipe, onClick = {})
                            }
                        }
                        CircularIndeterminateProgressBar(displayed = loading)
                    }
                }
            }
        }
    }
}