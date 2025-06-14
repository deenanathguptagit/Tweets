package com.deena.tweetsfeed.data.manager
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_datastore")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun putString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { it[dataStoreKey] = value }
    }

    fun getString(key: String, default: String? = null): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[dataStoreKey] ?: default }
    }

    suspend fun putInt(key: String, value: Int) {
        val dataStoreKey = intPreferencesKey(key)
        context.dataStore.edit { it[dataStoreKey] = value }
    }

    fun getInt(key: String, default: Int = 0): Flow<Int> {
        val dataStoreKey = intPreferencesKey(key)
        return context.dataStore.data.map { it[dataStoreKey] ?: default }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { it[dataStoreKey] = value }
    }

    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return context.dataStore.data.map { it[dataStoreKey] ?: default }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}