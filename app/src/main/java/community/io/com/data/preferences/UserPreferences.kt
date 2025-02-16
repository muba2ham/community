package community.io.com.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val dataStore: DataStore<Preferences>) {



    val userId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_ID]
        }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_USER_ID] = userId
        }
    }

    val userToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_TOKEN]
        }

     suspend fun saveToken(userId: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_USER_TOKEN] = userId
        }
    }




    val communityId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_COMMUNITY_ID]
        }



    suspend fun saveCommunityId(userId: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_COMMUNITY_ID] = userId
        }
    }


    val userName: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_USER_NAME]
        }

    suspend fun saveUsername(mobile: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_USER_NAME] = mobile
        }
    }

    val userMobile: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_MOBILE]
        }


    suspend fun saveUserMobile(mobile: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_MOBILE] = mobile
        }
    }

    val userEmail: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_EMAIL]
        }

    suspend fun saveUserEmail(mobile: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_EMAIL] = mobile
        }
    }


    val userAddress1: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_ADDRESS_1]
        }

    suspend fun saveUserAddress1(mobile: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[KEY_ADDRESS_1] = mobile
        }
    }

     suspend fun clear() {
        dataStore.edit { mutablePreferences -> mutablePreferences.clear()
        }
    }

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_TOKEN = stringPreferencesKey("token")
        private val KEY_COMMUNITY_ID = stringPreferencesKey("community_id")
        private val KEY_USER_NAME = stringPreferencesKey("username")
        private val KEY_MOBILE = stringPreferencesKey("mobile")
        private val KEY_ADDRESS_1 = stringPreferencesKey("address_1")
        private val KEY_EMAIL = stringPreferencesKey("email")
    }
}