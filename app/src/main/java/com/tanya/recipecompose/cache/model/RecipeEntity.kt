package com.tanya.recipecompose.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

    @ColumnInfo(name = "rating")
    var rating: Int,

    @ColumnInfo(name = "source_url")
    var sourceUrl: String,

    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    @ColumnInfo(name = "date_updated")
    var dateUpdated: Long,

    @ColumnInfo(name = "date_cached")
    var dateCached: Long,

)
