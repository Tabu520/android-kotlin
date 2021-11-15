package taipt4.kotlin.androidarchitectures.networking

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object CountriesService {

    private const val BASE_URL = "https://restcountries.eu/rest/v2/"

    fun create(): CountriesApi {
        val retrofit=  Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(CountriesApi::class.java)
    }
}