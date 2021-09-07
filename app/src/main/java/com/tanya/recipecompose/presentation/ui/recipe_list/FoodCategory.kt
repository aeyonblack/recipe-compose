package com.tanya.recipecompose.presentation.ui.recipe_list

import com.tanya.recipecompose.presentation.ui.recipe_list.FoodCategory.*


enum class FoodCategory(val value: String) {
    CHICKEN("Chicken"),
    SOUP("Soup"),
    BEEF("Beef"),
    DESSERT("Dessert"),
    PIZZA("Pizza"),
    DONUT("Donut"),
    MILK("Milk"),
    VEGAN("Vegan"),
    VEGETARIAN("Vegetarian")
}

fun getAllFoodCategories(): List<FoodCategory> {
    return listOf(CHICKEN, SOUP, BEEF, DESSERT,
    PIZZA, DONUT, MILK, VEGAN, VEGETARIAN)
}

fun getFoodCategory(value: String): FoodCategory? {
    val map = FoodCategory.values().associateBy(FoodCategory::value)
    return map[value]
}