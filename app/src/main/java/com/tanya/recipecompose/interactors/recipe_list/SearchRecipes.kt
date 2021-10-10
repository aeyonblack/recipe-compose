package com.tanya.recipecompose.interactors.recipe_list

import com.tanya.recipecompose.cache.RecipeDao
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.data.DataState
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import com.tanya.recipecompose.util.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Search and return all recipes from a given category
 */
@Suppress("RemoveExplicitTypeArguments")
class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun execute(
        token: String,
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            // for testing purposes
            if (query == "error") {
                throw Exception("Search Failed")
            }

            try {
                val recipes = getRecipesFromNetwork(
                    token = token,
                    page = page,
                    query = query
                )
                recipeDao.insertRecipes(entityMapper.toEntityList(recipes))
            } catch (e: Exception) {
                //e.printStackTrace()
                emit(DataState.error<List<Recipe>>(e.message?:"Unknown error"))
            }

            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(
                    pageSize = PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    page = page,
                    pageSize = PAGE_SIZE
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch(e: Exception) {
            emit(DataState.error<List<Recipe>>("request failed:${e.message?: "Unknown error"}"))
        }
    }

    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<Recipe> {
        return dtoMapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        )
    }
}