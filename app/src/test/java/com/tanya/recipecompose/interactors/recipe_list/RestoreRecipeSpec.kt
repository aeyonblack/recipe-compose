package com.tanya.recipecompose.interactors.recipe_list

import com.google.gson.GsonBuilder
import com.tanya.recipecompose.cache.AppDatabaseFake
import com.tanya.recipecompose.cache.RecipeDaoFake
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.network.MockWebServerResponses
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_OK

class RestoreRecipeSpec {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "dummy124token"
    private val dummyQuery = "dummy_query"

    private lateinit var restoreRecipes: RestoreRecipes

    private lateinit var searchRecipes: SearchRecipes
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private val entityMapper = RecipeEntityMapper()
    private val dtoMapper = RecipeDtoMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeService::class.java)
        recipeDaoFake = RecipeDaoFake(appDatabase)

        searchRecipes = SearchRecipes(
            recipeDao = recipeDaoFake,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )

        restoreRecipes = RestoreRecipes(
            recipeDao = recipeDaoFake,
            entityMapper = entityMapper
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(MockWebServerResponses.recipeListResponse)
        )
    }

    @Test
    @DisplayName("Cache is initially empty")
    fun cacheIsInitiallyEmpty(): Unit = runBlocking {
        assertThat(recipeDaoFake.getAllRecipes(1,30)).isEmpty()
    }

    @Test
    @DisplayName("Recipes from network response are cached")
    fun recipesFromNetworkResponseAreCached(): Unit = runBlocking {
        searchRecipes.execute(
            token = dummyToken,
            page = 1,
            query = dummyQuery
        ).toList()
        assertThat(recipeDaoFake.getAllRecipes(1,30)).isNotEmpty
    }

    @Test
    @DisplayName("recipes queried from network are restored through cache")
    fun recipesFromNetworkAreRestoredThroughCache(): Unit = runBlocking {
        searchRecipes.execute(dummyToken, 1, dummyQuery).toList()

        val flowItems = restoreRecipes.execute(
            page = 1,
            query = dummyQuery
        ).toList()

        val softly = SoftAssertions()
        softly.assertThat(flowItems[0].loading).isTrue

        val recipes = flowItems[1].data
        softly.assertThat(recipes).isNotNull
        softly.assertThat(recipes?.size?:0).isGreaterThan(0)
        softly.assertThat(recipes?.get(0)).isInstanceOf(Recipe::class.java)
        softly.assertThat(flowItems[1].loading).isFalse

        softly.assertAll()
    }

}