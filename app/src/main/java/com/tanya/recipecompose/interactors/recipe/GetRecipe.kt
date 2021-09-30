package com.tanya.recipecompose.interactors.recipe

import com.tanya.recipecompose.cache.RecipeDao
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.data.DataState
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * [Use-Case]
 * Get recipe from the cache given its unique id
 */
@Suppress("RemoveExplicitTypeArguments")
class GetRecipe(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
    private val recipeService: RecipeService,
    private val recipeDtoMapper: RecipeDtoMapper
) {
    fun execute(
        recipeId: Int,
        token: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading<Recipe>())

            // simulate a network delay
            delay(1000)

            var recipe = getRecipeFromCache(recipeId = recipeId)

            if (recipe != null) {
                emit(DataState.success(recipe))
            } else {
                /*we couldn't get recipe from cache, get it from the network
                * and cache it*/
                if (isNetworkAvailable) {
                    val networkRecipe = getRecipeFromNetwork(token, recipeId)
                    recipeDao.insertRecipe(entityMapper.mapFromDomainModel(networkRecipe))
                }
                recipe = getRecipeFromCache(recipeId)

                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    throw Exception("Unable to get recipe from cache")
                }
            }
        } catch (e: Exception) {
            emit(DataState.error<Recipe>(e.message?: "Unknown error"))
        }
    }

    /**
     * Get recipe from local room database
     */
    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let {
            entityMapper.mapToDomainModel(it)
        }
    }

    /**
     * Make a network request to get the recipe from API
     */
    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return recipeDtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }

}