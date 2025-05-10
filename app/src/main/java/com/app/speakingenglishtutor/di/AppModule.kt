package com.app.speakingenglishtutor.di

import android.content.Context
import com.app.speakingenglishtutor.BuildConfig
import com.app.speakingenglishtutor.data.repository.GrammarRepository
import com.app.speakingenglishtutor.data.repository.GrammarRepositoryImpl
import com.google.ai.client.generativeai.GenerativeModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(@ApplicationContext context: Context): GenerativeModel {
        // Note: You need to add your API key to local.properties as GEMINI_API_KEY
        // and configure the secrets gradle plugin to access it
        return GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = BuildConfig.apiKey
        )
    }

    @Provides
    @Singleton
    fun provideGrammarRepository(generativeModel: GenerativeModel): GrammarRepository {
        return GrammarRepositoryImpl(generativeModel)
    }
}
