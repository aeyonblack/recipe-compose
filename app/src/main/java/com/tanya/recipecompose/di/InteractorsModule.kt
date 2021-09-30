package com.tanya.recipecompose.di

import com.tanya.recipecompose.cache.RecipeDao
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.interactors.recipe.GetRecipe
import com.tanya.recipecompose.interactors.recipe_list.RestoreRecipes
import com.tanya.recipecompose.interactors.recipe_list.SearchRecipes
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeDao: RecipeDao,
        recipeService: RecipeService,
        entityMapper: RecipeEntityMapper,
        dtoMapper: RecipeDtoMapper
    ): SearchRecipes {
        return SearchRecipes(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        entityMapper: RecipeEntityMapper
    ): RestoreRecipes {
        return RestoreRecipes(
            recipeDao = recipeDao,
            entityMapper = entityMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeDao: RecipeDao,
        recipeService: RecipeService,
        entityMapper: RecipeEntityMapper,
        dtoMapper: RecipeDtoMapper
    ): GetRecipe {
        return GetRecipe(
            recipeDao = recipeDao,
            recipeService = recipeService,
            recipeDtoMapper = dtoMapper,
            entityMapper = entityMapper
        )
    }

}