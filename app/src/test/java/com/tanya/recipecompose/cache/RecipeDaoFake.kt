package com.tanya.recipecompose.cache

import com.tanya.recipecompose.cache.model.RecipeEntity

class RecipeDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): RecipeDao {
    override suspend fun insertRecipe(recipe: RecipeEntity): Long {
        appDatabaseFake.recipes.add(recipe)
        return 1 // success
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        appDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1)
    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return appDatabaseFake.recipes.find { it.id == id }
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        appDatabaseFake.recipes.removeIf { it.id in ids }
        return 1
    }

    override suspend fun deleteAllRecipes() {
        appDatabaseFake.recipes.clear()
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        appDatabaseFake.recipes.removeIf { it.id == primaryKey }
        return 1
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

}