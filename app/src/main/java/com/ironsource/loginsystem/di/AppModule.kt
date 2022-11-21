package com.ironsource.loginsystem.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.ironsource.loginsystem.auth.AuthService
import com.ironsource.loginsystem.auth.AuthServiceImpl
import com.ironsource.loginsystem.data.local.LocalDataSource
import com.ironsource.loginsystem.data.local.LoginSystemDatabase
import com.ironsource.loginsystem.data.local.PostsDao
import com.ironsource.loginsystem.data.remote.JsonPlaceHolderApi
import com.ironsource.loginsystem.data.remote.RemoteDataSource
import com.ironsource.loginsystem.repository.Repository
import com.ironsource.loginsystem.repository.RepositoryImpl
import com.ironsource.loginsystem.util.FileHelper
import com.ironsource.loginsystem.util.SharedPrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Database
    private const val MOVIE_DB = "movie_db"

    //API
    private const val HTTP_API_KEY = "api_key"
    private const val API_KEY = "cbc8ee7aac105c070f5f830d4e2c2f0b"
    private const val API_ENDPOINT = "https://jsonplaceholder.typicode.com/"

    @Singleton
    @Provides
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ) = Room
        .databaseBuilder(
            context.applicationContext,
            LoginSystemDatabase::class.java,
            MOVIE_DB
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(
        retrofit: Retrofit
    ): JsonPlaceHolderApi {
        return retrofit.create(JsonPlaceHolderApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOksHttpClient(
        okHttpLogger: HttpLoggingInterceptor,
//        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(okHttpLogger)
            .build()
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request();
            val url = request.url.newBuilder()
                .addQueryParameter(HTTP_API_KEY, API_KEY)
                .build();
            val newRequest = request.newBuilder().url(url).build();
            chain.proceed(newRequest)
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        loginSystemDatabase: LoginSystemDatabase
    ): Repository = RepositoryImpl(
        remoteDataSource,
        localDataSource,
        loginSystemDatabase
    )

    @Singleton
    @Provides
    fun provideLocalDataSource(
        fileHelper: FileHelper,
        @ApplicationContext context: Context,
        sharedPrefManager: SharedPrefManager,
        database: LoginSystemDatabase
    ): LocalDataSource = LocalDataSource(
        fileHelper,
        sharedPrefManager,
        context,
        database
    )


    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideAuthService(repository: Repository): AuthService {
        return AuthServiceImpl(repository)
    }


}