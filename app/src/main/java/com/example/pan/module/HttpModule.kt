package com.example.pan.module

import android.content.Context
import com.example.pan.http.IPanService
import com.example.pan.http.PanRepository
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
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
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(Interceptor {
                val request = it.request()
                val url = request.url.newBuilder()
                url.addEncodedQueryParameter("web", "1")
                url.addEncodedQueryParameter("t", "0.9272407329780168")
                url.addEncodedQueryParameter("channel", "chunlei")
                url.addEncodedQueryParameter("app_id", "250528")
                url.addEncodedQueryParameter("bdstoken", "51bf181d1ee64ca6b9deda56ed0702e5")
                url.addEncodedQueryParameter("logid", "ODJFNDMxMDFFRjE3Q0Y0NjhFQ0EyMTQzM0NGOTg3NTc6Rkc9MQ==")
                url.addEncodedQueryParameter("clienttype", "0")
                val newBuilder = request.newBuilder()
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