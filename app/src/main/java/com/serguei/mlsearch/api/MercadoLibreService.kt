package com.serguei.mlsearch.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.serguei.mlsearch.model.Description
import com.serguei.mlsearch.model.Item
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.itishka.gsonflatten.FlattenTypeAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val TAG = "MercadoLibreService"

/**
 * Search items based on a query
 */
fun searchItems(
        service: MercadoLibreService,
        query: String,
        offset: Int,
        onSuccess: (items: List<Item>) -> Unit,
        onError: (error: String) -> Unit) {

    Log.d(TAG, "query: $query")

    service.searchItems(query, offset).enqueue(
            object : Callback<ItemSearchResponse> {
                override fun onFailure(call: Call<ItemSearchResponse>?, t: Throwable) {
                    Log.d(TAG, "failed to make the API call: ${t.localizedMessage}")
                    onError(t.localizedMessage ?: "unknown error")
                }

                override fun onResponse(call: Call<ItemSearchResponse>?, response: Response<ItemSearchResponse>) {
                    Log.d(TAG, "response $response")
                    if (response.isSuccessful) {
                        val items = response.body()?.items ?: emptyList()
                        onSuccess(items)
                    } else {
                        onError(response.errorBody()?.string() ?: "unknown error")
                    }
                }

            }
    )
}

fun getItemDescription(
        service: MercadoLibreService,
        itemId: String,
        onSuccess: (description: Description) -> Unit,
        onError: (error: String) -> Unit) {

    service.getItemDescription(itemId).enqueue(
            object : Callback<Description> {
                override fun onFailure(call: Call<Description>?, t: Throwable) {
                    Log.d(TAG, "failed to make the API call: ${t.localizedMessage}")
                    onError(t.localizedMessage ?: "unknown error")
                }

                override fun onResponse(call: Call<Description>?, response: Response<Description>) {
                    Log.d(TAG, "response $response")
                    if (response.isSuccessful) {
                        val description = response.body()
                        onSuccess(description ?: Description(""))
                    } else {
                        onError(response.errorBody()?.string() ?: "unknown error")
                    }
                }

            }
    )
}

/**
 * MercadoLibre API communication using Retrofit
 */
interface MercadoLibreService {
    /**
     * Search items based on a query
     */
    @GET("sites/MLA/search")
    fun searchItems(@Query("q") query: String,
                    @Query("offset") offset: Int): Call<ItemSearchResponse>

    @GET("items/{item_id}/description")
    fun getItemDescription(@Path("item_id") itemId: String): Call<Description>

    companion object {
        private const val BASE_URL = "https://api.mercadolibre.com/"

        fun create(): MercadoLibreService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()

            val gson = GsonBuilder()
                    .registerTypeAdapterFactory(FlattenTypeAdapterFactory())
                    .create()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(MercadoLibreService::class.java)
        }
    }
}