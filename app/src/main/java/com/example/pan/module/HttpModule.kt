package com.example.pan.module

import android.content.Context
import com.example.pan.http.IPanService
import com.example.pan.http.PanRepository
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
        val mmkv = MMKV.mmkvWithID("ACCOUNT", MMKV.MULTI_PROCESS_MODE)
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor {
                val request = it.request()
                val url = request.url.newBuilder()
                 url.addEncodedQueryParameter("access_token", mmkv?.decodeString("access_token"))
                val newBuilder = request.newBuilder()
                newBuilder.addHeader("User-Agent", "pan.baidu.com")
                newBuilder.url(url.build())
                it.proceed(newBuilder.build())
            })
            .addInterceptor(interceptor)
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
    fun providePanService(retrofit: Retrofit): IPanService {
        return retrofit.create(IPanService::class.java)
    }

    @Provides
    @Singleton
    fun providePanRepository(service: IPanService): PanRepository {
        return PanRepository(service)
    }
}