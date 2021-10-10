package com.tanya.recipecompose.interactors

import com.tanya.recipecompose.cache.RecipeDaoFake
import com.tanya.recipecompose.interactors.recipe_list.SearchRecipes
import com.tanya.recipecompose.network.MockWebServerResponses
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection.HTTP_OK

interface BaseTest {

    fun getDaoFake(): RecipeDaoFake
    fun getMockWebServer(): MockWebServer
    fun getSearchRecipes(): SearchRecipes
    fun getToken(): String
    fun getQuery(): String

    @Test
    @DisplayName("cache is initially empty")
    fun cacheIsInitiallyEmpty(): Unit = runBlocking {
        assertThat(getDaoFake().getAllRecipes(1,30)).isEmpty()
    }

    @Test
    @DisplayName("Recipes from network response are cached")
    fun recipesFromNetworkResponseAreCached(): Unit = runBlocking {
        getMockWebServer().enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(MockWebServerResponses.recipeListResponse)
        )

        getSearchRecipes().execute(
            token = getToken(),
            page = 1,
            query = getQuery()
        ).toList()

        assertThat(getDaoFake().getAllRecipes(1,30)).isNotEmpty
    }

    @AfterEach
    fun tearDown() {
        getMockWebServer().shutdown()
    }

}