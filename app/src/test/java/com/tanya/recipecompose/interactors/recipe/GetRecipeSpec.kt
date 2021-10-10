package com.tanya.recipecompose.interactors.recipe

import com.google.gson.GsonBuilder
import com.tanya.recipecompose.cache.AppDatabaseFake
import com.tanya.recipecompose.cache.RecipeDaoFake
import com.tanya.recipecompose.cache.model.RecipeEntityMapper
import com.tanya.recipecompose.domain.model.Recipe
import com.tanya.recipecompose.interactors.BaseTest
import com.tanya.recipecompose.interactors.recipe_list.SearchRecipes
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
import java.net.HttpURLConnection

class GetRecipeSpec: BaseTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val dummyToken = "ab3182fjd2kb3892"
    private val dummyQuery = "a dummy query"

    private lateinit var getRecipe: GetRecipe
    private val recipeId = 1551

    private lateinit var searchRecipes: SearchRecipes
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

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

        // instantiate the system under test
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
        @DisplayName("flow emits: loading = true, recipe data " +
                "and loading = false respectively")
        fun validateFlowEmissions(): Unit = runBlocking {
            searchRecipes.execute(dummyToken, 1, dummyQuery).toList()

            val recipeAsFlow = getRecipe.execute(
                recipeId,
                dummyToken,
                true
            ).toList()

            val softly = SoftAssertions()
            softly.assertThat(recipeAsFlow[0].loading).isTrue

            val recipe = recipeAsFlow[1].data
            softly.assertThat(recipe).isNotNull
            softly.assertThat(recipe?.id).isEqualTo(recipeId)
            softly.assertThat(recipe).isInstanceOf(Recipe::class.java)
            softly.assertThat(recipeAsFlow[1].loading).isFalse

            softly.assertAll()
        }

    }

    @Nested
    @DisplayName("When a recipe is queried by id from cache")
    inner class NullRecipeQueriedFromCache {

        @BeforeEach
        fun setup() {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(MockWebServerResponses.recipeWithId1551)
            )
        }

        @Test
        @DisplayName("flow emits: loading = true, recipe data " +
                "and loading = false respectively")
        fun validateFlowEmissions(): Unit = runBlocking {
            val recipeAsFlow = getRecipe.execute(
                recipeId = recipeId,
                token = dummyToken,
                isNetworkAvailable = true
            ).toList()

            val softly = SoftAssertions()
            softly.assertThat(recipeDaoFake.getAllRecipes(1,30)).isNotEmpty
            softly.assertThat(recipeAsFlow[0].loading).isTrue

            val recipe = recipeAsFlow[1].data
            softly.assertThat(recipe?.id).isEqualTo(recipeId)
            softly.assertThat(recipeDaoFake.getRecipeById(recipeId)?.id).isEqualTo(recipeId)
            softly.assertThat(recipe).isInstanceOf(Recipe::class.java)
            softly.assertThat(recipeAsFlow[1].loading).isFalse

            softly.assertAll()
        }

    }

}