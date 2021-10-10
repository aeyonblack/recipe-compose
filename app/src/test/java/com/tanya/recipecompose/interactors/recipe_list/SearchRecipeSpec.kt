package com.tanya.recipecompose.interactors.recipe_list

import com.google.gson.GsonBuilder
import com.tanya.recipecompose.cache.AppDatabaseFake
import com.tanya.recipecompose.cache.RecipeDaoFake
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.interactors.BaseTest
import com.tanya.recipecompose.network.MockWebServerResponses
import com.tanya.recipecompose.network.RecipeService
import com.tanya.recipecompose.network.model.RecipeDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_OK

class SearchRecipeSpec: BaseTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "dummy124token"
    private val dummyQuery = "dummy_chicken_query"

    private lateinit var searchRecipes: SearchRecipes

    private lateinit var recipeService: RecipeService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private var dtoMapper = RecipeDtoMapper()
    private var entityMapper = RecipeEntityMapper()

    override fun getDaoFake(): RecipeDaoFake = recipeDaoFake
    override fun getMockWebServer(): MockWebServer = mockWebServer
    override fun getSearchRecipes(): SearchRecipes = searchRecipes
    override fun getToken(): String = dummyToken
    override fun getQuery(): String = dummyQuery

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
    }

    @Nested
    @DisplayName("When recipes are queried from network")
    inner class EmitRecipesFromCache {

        @BeforeEach
        fun setup() {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HTTP_OK)
                    .setBody(MockWebServerResponses.recipeListResponse)
            )
        }

        @Test
        @DisplayName("flow emits: loading = true, recipe data " +
                "and loading = false respectively")
        fun validateFlowEmission(): Unit = runBlocking {
            val recipesAsFlow = searchRecipes.execute(dummyToken, 1, dummyQuery).toList()

            val softly = SoftAssertions()
            softly.assertThat(recipesAsFlow[0].loading).isTrue

            val recipes = recipesAsFlow[1].data
            softly.assertThat(recipes).isNotNull
            softly.assertThat(recipes?.size?: 0).isGreaterThan(0)
            softly.assertThat(recipes?.get(0)).isInstanceOf(Recipe::class.java)
            softly.assertThat(recipesAsFlow[1].loading).isFalse

            softly.assertAll()
        }
    }

    @Nested
    @DisplayName("When network response returns nothing")
    inner class EmitHttpError {

        @BeforeEach
        fun setup() {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HTTP_BAD_REQUEST)
                    .setBody(MockWebServerResponses.emptyResponse)
            )
        }

        @Test
        @DisplayName("flow should emit an http error")
        fun validateFlowEmission(): Unit = runBlocking {
            val flowItems = searchRecipes.execute(dummyToken, 1, dummyQuery).toList()

            val softly = SoftAssertions()
            softly.assertThat(flowItems[0].loading).isTrue

            val error = flowItems[1].error
            softly.assertThat(error).isNotNull
            softly.assertThat(flowItems[1].loading).isFalse

            softly.assertAll()
        }
    }
}