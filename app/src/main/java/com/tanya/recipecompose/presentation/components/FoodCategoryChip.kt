package com.tanya.recipecompose.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tanya.recipecompose.presentation.ui.recipe_list.FoodCategory
import kotlinx.coroutines.launch

@Composable
fun FoodCategoryMenu(
    categories: List<FoodCategory>,
    selectedCategory: FoodCategory?,
    categoryScrollPosition: Int,
    onExecuteSearch: () -> Unit,
    onSelectedCategoryChange: (String) -> Unit,
    onChangeCategoryScrollPosition: (Int) -> Unit
) {
    // remember the position to scroll to
    val listState = rememberLazyListState()

    // remember a coroutine scope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 8.dp),
        state = listState
        ) {

        // scroll to category position
        coroutineScope.launch {
            listState.scrollToItem(categoryScrollPosition)
        }

        // compose all category chips
        items(categories) {category ->
            FoodCategoryChip(
                category = category.value,
                onExecuteSearch = onExecuteSearch,
                selected = selectedCategory == category,
                onSelectedCategoryChange = {
                    onSelectedCategoryChange(it)
                    // save the current list offset
                    onChangeCategoryScrollPosition(listState
                        .firstVisibleItemScrollOffset)
                }
            )
        }
    }
}

@Composable
fun FoodCategoryChip(
    category: String,
    selected: Boolean = false,
    onSelectedCategoryChange: (String) -> Unit,
    onExecuteSearch: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (selected) Color.LightGray else MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier.toggleable(
            value = selected,
            onValueChange = {
                onSelectedCategoryChange(category)
                onExecuteSearch()
            }
        )) {
            Text(
                text = category,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}