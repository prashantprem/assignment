package com.example.moengageassignment.di

import com.example.moengageassignment.data.repository.NewsRepository
import com.example.moengageassignment.data.repository.NewsRepositoryImpl
import com.example.moengageassignment.data.sources.NewsRemoteDataSource
import com.example.moengageassignment.data.sources.NewsRemoteDataSourceImpl
import com.example.moengageassignment.domain.GetNewsArticlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl()
    }


    @Provides
    @Singleton
    fun provideNewsRepository(newsRemoteDataSource: NewsRemoteDataSource): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetNewsArticlesUseCase(repository: NewsRepository): GetNewsArticlesUseCase {
        return GetNewsArticlesUseCase(repository)
    }


}