package com.example.pan.module

import com.example.pan.aria2.Aria2Repository
import com.example.pan.data.Account
import com.example.pan.http.ApiService
import com.example.pan.http.PanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class HttpModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val aria2Url = "http://127.0.0.1:6800/jsonrpc"
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor {
                val request = it.request()
                if (request.header("baseUrl") == null) {
                    val url = request.url.newBuilder()
                    url.addQueryParameter("access_token", Account.getAccessToken())
                    val newBuilder = request.newBuilder()
                    newBuilder.addHeader("User-Agent", "pan.baidu.com")
                    newBuilder.url(url.build())
                    it.proceed(newBuilder.build())
                } else {
                    it.proceed(request.newBuilder().url(aria2Url).build())
                }
            })
            // .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://pan.baidu.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePanService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePanRepository(service: ApiService): PanRepository {
        return PanRepository(service)
    }

    @Provides
    @Singleton
    fun provideAria2Repository(service: ApiService): Aria2Repository {
        return Aria2Repository(service)
    }
}