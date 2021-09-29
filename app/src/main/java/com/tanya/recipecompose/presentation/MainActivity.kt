package com.tanya.recipecompose.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tanya.recipecompose.presentation.navigation.Screen
import com.tanya.recipecompose.presentation.ui.recipe.RecipeDetailScreen
import com.tanya.recipecompose.presentation.ui.recipe.RecipeViewModel
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListScreen
import com.tanya.recipecompose.presentation.ui.recipe_list.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // create a NavController
            val navController = rememberNavController()

            // create a NavHost
            NavHost(
                navController = navController,
                startDestination = Screen.RecipeList.route
            ) {

                /*recipe list screen*/
                composable(route = Screen.RecipeList.route) {
                    val factory = HiltViewModelFactory(LocalContext.current, it)
                    val viewModel: RecipeListViewModel =
                        viewModel(key = "RecipeListViewModel", factory = factory)
                    RecipeListScreen(
                        onNavigateToRecipeDetailScreen = navController::navigate,
                        viewModel = viewModel
                    )
                }

                /*recipe detail screen*/
                composable(
                    route = Screen.RecipeDetail.route + "/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.IntType
                    })
                ) {
                    val factory = HiltViewModelFactory(LocalContext.current, it)
                    val viewModel: RecipeViewModel =
                        viewModel(key = "RecipeViewModel", factory = factory)
                    RecipeDetailScreen(
                        recipeId = it.arguments?.getInt("recipeId"),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
