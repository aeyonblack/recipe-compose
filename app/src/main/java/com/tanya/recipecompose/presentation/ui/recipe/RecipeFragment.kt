package com.tanya.recipecompose.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tanya.recipecompose.presentation.BaseApplication
import com.tanya.recipecompose.presentation.components.CircularIndeterminateProgressBar
import com.tanya.recipecompose.presentation.components.RecipeView
import com.tanya.recipecompose.presentation.ui.recipe.RecipeEvent.*
import com.tanya.recipecompose.ui.theme.RecipeComposeTheme
import com.tanya.recipecompose.util.SnackbarController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject lateinit var app: BaseApplication

    private val viewModel:RecipeViewModel by viewModels()

    private val snackbarController = SnackbarController(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Main).launch {
            delay(1000)
            arguments?.getInt("recipeId")?.let {
                viewModel.onTriggerEvent(GetRecipeEvent(it))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply { 
            setContent {
                val recipe = viewModel.recipe.value
                val loading = viewModel.loading.value
                val scaffoldState = rememberScaffoldState()

                RecipeComposeTheme(darkTheme = app.isDark.value) {
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
        }
    }
}