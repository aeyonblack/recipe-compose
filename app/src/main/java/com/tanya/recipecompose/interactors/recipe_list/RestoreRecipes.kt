package com.tanya.recipecompose.interactors.recipe_list

import com.tanya.recipecompose.cache.RecipeDao
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.data.DataState
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.util.PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Restore the list of recipes after process death
 */
@Suppress("RemoveExplicitTypeArguments")
class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper
) {
    fun execute(
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            // simulate network delay
            delay(1000)

            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message?:"Unknown error"))
        }
    }
}