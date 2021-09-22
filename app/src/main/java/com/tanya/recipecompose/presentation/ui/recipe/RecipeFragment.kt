package com.tanya.recipecompose.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tanya.recipecompose.presentation.ui.recipe.RecipeEvent.*
import com.tanya.recipecompose.ui.theme.RecipeComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val viewModel:RecipeViewModel by viewModels()

    private var recipeId: MutableState<Int?> = mutableStateOf(null)

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

                RecipeComposeTheme {
                    Column(modifier = Modifier.padding(all=16.dp)) {
                        Text(
                            text =
                            recipe?.let { "${it.title}" }?:"loading..."
                        )
                    }
                }
            }
        }
    }
}