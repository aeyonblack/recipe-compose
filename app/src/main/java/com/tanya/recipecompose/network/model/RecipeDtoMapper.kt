package com.tanya.recipecompose.network.model

import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.domain.util.DomainMapper
import com.tanya.recipecompose.util.DateUtils

/**
 * Maps recipe data from the REST API
 * to our main business model of the recipe
 */
class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {
    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk,
            title = model.title,
            featuredImage = model.featuredImage,
            rating = model.rating,
            publisher = model.publisher,
            sourceUrl = model.sourceUrl,
            ingredients = model.ingredients,
            dateAdded = DateUtils.longToDate(model.longDateAdded),
            dateUpdated = DateUtils.longToDate(model.longDateUpdated),
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            title = domainModel.title,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            publisher = domainModel.publisher,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            longDateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeDto>{
        return initial.map { mapFromDomainModel(it) }
    }

}