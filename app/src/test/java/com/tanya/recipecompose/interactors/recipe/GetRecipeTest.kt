package com.tanya.recipecompose.interactors.recipe

import com.google.gson.GsonBuilder
import com.tanya.recipecompose.cache.AppDatabaseFake
import com.tanya.recipecompose.cache.RecipeDaoFake
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.interactors.recipe_list.SearchRecipes
import com.tanya.recipecompose.network.MockWebServerResponses
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import org.assertj.core.api.Assertions.assertThat

class GetRecipeTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "ab3182fjd2kb3892"

    /*System Under Test - We're testing the GetRecipe uses case*/
    private lateinit var getRecipe: GetRecipe
    private val recipeId = 1551

    /*Dependencies*/
    private lateinit var searchRecipes: SearchRecipes
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService  = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory
                .create(GsonBuilder().create()))
            .build()
            .create(RecipeService::class.java)
        recipeDaoFake = RecipeDaoFake(appDatabase)

        initSystem()
    }

    private fun initSystem() {
        searchRecipes = SearchRecipes(
            recipeDao = recipeDaoFake,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )

        // instantiate the system in test
        getRecipe = GetRecipe(
            recipeDao = recipeDaoFake,
            entityMapper = entityMapper,
            recipeService = recipeService,
            recipeDtoMapper = dtoMapper,
        )
    }

    @Nested
    @DisplayName("When recipes are queried from the network/API")
    inner class RecipesQueriedFromNetwork {

        @BeforeEach
        fun setup() {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(MockWebServerResponses.recipeListResponse)
            )
        }

        @Test
        @DisplayName("the cache is initially empty")
        fun cacheIsInitiallyEmpty(): Unit = runBlocking {
            assertThat(recipeDaoFake.getAllRecipes(1,30)).isEmpty()
        }

    }




}