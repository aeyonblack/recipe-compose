package com.tanya.recipecompose.di

import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import com.tanya.recipecompose.repository.RecipeRepository
import com.tanya.recipecompose.repository.RecipeRepository_Impl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRecipeRepository(
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepository_Impl(recipeService, recipeDtoMapper)
    }

}