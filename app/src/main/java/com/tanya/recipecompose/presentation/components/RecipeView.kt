package com.tanya.recipecompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.util.DEFAULT_RECIPE_IMAGE
import com.tanya.recipecompose.util.IMAGE_HEIGHT
import com.tanya.recipecompose.util.loadPicture

@Composable
fun RecipeView(
    recipe: Recipe
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        recipe.featuredImage.let {
            val image = loadPicture(
                url = it,
                defaultImage = DEFAULT_RECIPE_IMAGE
            ).value
            image?.let { img ->
                Image(
                    bitmap = img.asImageBitmap(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(IMAGE_HEIGHT.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "recipe featured image"
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            recipe.title.let {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h4
                    )
                    val rank = recipe.rating.toString()
                    Text(
                        text = rank,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
            recipe.publisher.let {
                val updated = recipe.dateUpdated
                Text(
                    text = "Updated $updated by $it",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            for (ingredient in recipe.ingredients) {
                Text(
                    text = ingredient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}