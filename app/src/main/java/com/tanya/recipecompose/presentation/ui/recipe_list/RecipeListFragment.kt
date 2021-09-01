package com.tanya.recipecompose.presentation.ui.recipe_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tanya.recipecompose.R
import com.tanya.recipecompose.ui.theme.RecipeComposeTheme
import com.tanya.recipecompose.util.TAG
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
                val recipes = viewModel.recipes.value
                for (recipe in recipes) {
                    Log.d(TAG, "onCreateView: ${recipe.title}")
                }
                RecipeComposeTheme {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        Text(text = "RecipeListFragment")
                        Spacer(modifier = Modifier.padding(16.dp))
                        Button(onClick = { findNavController().navigate(R.id.view_recipe) }) {
                            Text(text = "To Recipe Fragment")
                        }
                    }
                }
            }
        }
    }
}