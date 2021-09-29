package com.tanya.recipecompose.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tanya.recipecompose.cache.RecipeDao
import com.tanya.recipecompose.cache.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        const val DATABASE_NAME = "recipe_db"
    }

}