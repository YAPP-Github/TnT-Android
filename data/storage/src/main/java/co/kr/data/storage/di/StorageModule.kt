package co.kr.data.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object StorageModule {
    private const val SESSION_STORAGE_NAME = "SESSION_STORAGE"
    private val Context.sessionDataStore by preferencesDataStore(name = SESSION_STORAGE_NAME)

    private const val USER_STORAGE_NAME = "USER_STORAGE"
    private val Context.userDataStore by preferencesDataStore(name = USER_STORAGE_NAME)

    @Provides
    @Singleton
    @Named("Session")
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.sessionDataStore

    @Provides
    @Singleton
    @Named("User")
    fun provideUserLocalDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore
}
