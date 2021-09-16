package com.tanya.recipecompose.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
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
import androidx.lifecycle.lifecycleScope
import com.tanya.recipecompose.presentation.BaseApplication
import com.tanya.recipecompose.presentation.components.*
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListEvent.*
import com.tanya.recipecompose.ui.theme.RecipeComposeTheme
import com.tanya.recipecompose.util.PAGE_SIZE
import com.tanya.recipecompose.util.SnackbarController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var app: BaseApplication

    val viewModel: RecipeListViewModel by viewModels()

    private val snackbarController = SnackbarController(lifecycleScope)

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
                val page = viewModel.page.value

                // focus manager
                val focusManager = LocalFocusManager.current

                val scaffoldState = rememberScaffoldState()

                RecipeComposeTheme(darkTheme = app.isDark.value) {
                    Scaffold(
                        topBar = {
                            SearchBar(
                                query = query,
                                selectedCategory = selectedCategory,
                                categoryScrollPosition = viewModel.categoryScrollPosition,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = {
                                    if (viewModel.selectedCategory.value?.value == "Milk") {
                                        lifecycleScope.launch {
                                            snackbarController.showSnackbar(
                                                scaffoldState = scaffoldState,
                                                message = "Invalid Category: MILK!",
                                                label = "DISMISS"
                                            )
                                        }
                                    } else {
                                        viewModel.onTriggerEvent(NewSearchEvent)
                                    }
                                },
                                onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                focusManager = focusManager,
                                onToggleTheme = app::toggleTheme
                            )
                        },
                        scaffoldState = scaffoldState,
                        snackbarHost = {scaffoldState.snackbarHostState}
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)) {
                            LazyColumn {
                                itemsIndexed(items = recipes) {index, recipe ->
                                    viewModel.onChangeRecipeScrollPosition(index)
                                    if ((index + 1) >= (page* PAGE_SIZE) && !loading)
                                        viewModel.onTriggerEvent(NextPageEvent)
                                    RecipeCard(recipe = recipe, onClick = {})
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
                }
            }
        }
    }
}