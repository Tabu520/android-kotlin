package com.taipt.mp3tik.di

import android.content.Context
import android.os.Build
import com.taipt.mp3tik.data.network.session.CookieSavingInterceptor
import com.taipt.mp3tik.BuildConfig
import com.taipt.mp3tik.data.local.save.video.SaveVideoFile
import com.taipt.mp3tik.data.local.save.video.SaveVideoFileApi29
import com.taipt.mp3tik.data.local.save.video.SaveVideoFileBelowApi29
import com.taipt.mp3tik.data.network.TikTokApi
import com.taipt.mp3tik.data.network.parsing.TikTokWebPageConverterFactory
import com.taipt.mp3tik.data.network.parsing.converter.ThrowIfIsCaptchaResponse
import com.taipt.mp3tik.data.repository.TiktokDownloadRepository
import com.taipt.mp3tik.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val throwIfIsCaptchaResponse: ThrowIfIsCaptchaResponse
        get() = ThrowIfIsCaptchaResponse()

    private val tikTokConverterFactory: Converter.Factory
        get() = TikTokWebPageConverterFactory(throwIfIsCaptchaResponse)

    private val cookieSavingInterceptor: CookieSavingInterceptor by lazy { CookieSavingInterceptor() }

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
//            .addInterceptor(cookieSavingInterceptor)
            .let {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                } else {
                    it
                }
            }
            .build()

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideTiktokDownloadRepository(api: TikTokApi, saveVideoFile: SaveVideoFile) = TiktokDownloadRepository(api, saveVideoFile)

    @Singleton
    @Provides
    fun provideTiktokApi(): TikTokApi {
        return retrofit.create(TikTokApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSaveVideoFile(
        @ApplicationContext app: Context
    ): SaveVideoFile =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SaveVideoFileApi29(app.contentResolver)
        } else {
            SaveVideoFileBelowApi29(app.contentResolver)
        }
}