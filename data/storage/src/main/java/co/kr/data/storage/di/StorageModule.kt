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
    const val SESSION_STORAGE_NAME = "SESSION_STORAGE"
    private val Context.sessionDataStore by preferencesDataStore(name = SESSION_STORAGE_NAME)

    const val SETTING_STORAGE_NAME = "SETTING_STORAGE"
    private val Context.settingDataStore by preferencesDataStore(name = SETTING_STORAGE_NAME)

    const val CONNECT_STORAGE_NAME = "HOME_STORAGE"
    private val Context.connectDataStore by preferencesDataStore(name = CONNECT_STORAGE_NAME)

    @Provides
    @Singleton
    @Named(SESSION_STORAGE_NAME)
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.sessionDataStore

    @Provides
    @Singleton
    @Named(SETTING_STORAGE_NAME)
    fun provideSettingDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.settingDataStore

    @Provides
    @Singleton
    @Named(CONNECT_STORAGE_NAME)
    fun provideConnectDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.connectDataStore
}
