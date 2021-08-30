package com.tanya.recipecompose.network.responses

import com.google.gson.annotations.SerializedName
import com.tanya.recipecompose.network.model.RecipeDto

data class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int?,

    @SerializedName("results")
    var recipes: List<RecipeDto>
)