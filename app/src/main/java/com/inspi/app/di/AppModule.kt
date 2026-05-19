package com.inspi.app.di

import android.content.Context
import androidx.room.Room
import com.inspi.app.BuildConfig
import com.inspi.app.data.local.InspiDatabase
import com.inspi.app.network.CoachApiService
import com.inspi.app.network.GeminiCoachService
import com.inspi.app.utils.Analytics
import com.inspi.app.utils.NoOpAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InspiDatabase =
        Room.databaseBuilder(context, InspiDatabase::class.java, "inspi.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserProfileDao(db: InspiDatabase) = db.userProfileDao()
    @Provides fun provideSubmissionDao(db: InspiDatabase) = db.submissionDao()
    @Provides fun provideChallengeDao(db: InspiDatabase) = db.challengeDao()
    @Provides fun provideCoachMessageDao(db: InspiDatabase) = db.coachMessageDao()
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideGeminiApiKey(): String = BuildConfig.GEMINI_API_KEY

    @Provides @Singleton
    fun provideCoachApiService(apiKey: String): CoachApiService =
        GeminiCoachService(apiKey)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds @Singleton
    abstract fun bindAnalytics(impl: NoOpAnalytics): Analytics
}
