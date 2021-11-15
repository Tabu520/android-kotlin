package taipt4.kotlin.androidarchitectures.networking

import io.reactivex.Single
import retrofit2.http.GET
import taipt4.kotlin.androidarchitectures.model.Country

interface CountriesApi {

    @GET("all")
    fun getCountries(): Single<List<Country>>
}