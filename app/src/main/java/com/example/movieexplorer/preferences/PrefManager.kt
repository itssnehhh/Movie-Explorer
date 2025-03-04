package com.example.movieexplorer.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.movieexplorer.constants.Constants.LOGIN_SCREEN
import com.example.movieexplorer.constants.Constants.ON_BOARD_SCREEN
import com.example.movieexplorer.model.FavoriteMovie
import com.example.movieexplorer.model.MovieDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {

    companion object {
        private const val PREF_NAME = "com.example.movieexplorer"
        private const val KEY_ONBOARD = ON_BOARD_SCREEN
        private const val KEY_LOGIN = LOGIN_SCREEN
        private const val USER_EMAIL = "userEmail"
        private const val USER_PASSWORD = "userPassword"
        private const val FAVOURITE_MOVIE = "favouriteMovie"
    }

    private var preferences: Lazy<SharedPreferences> = lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setOnBoardingStatus(status: Boolean) {
        preferences.value.edit().putBoolean(KEY_ONBOARD, status).apply()
    }

    fun getOnBoardingStatus(): Boolean {
        return preferences.value.getBoolean(KEY_ONBOARD, false)
    }

    fun setLoginStatus(status: Boolean) {
        preferences.value.edit().putBoolean(KEY_LOGIN, status).apply()
    }

    fun getLoginStatus(): Boolean {
        return preferences.value.getBoolean(KEY_LOGIN, false)
    }

    fun saveUserDetail(email: String, password: String) {
        preferences.value.edit().putString(USER_EMAIL, email).putString(USER_PASSWORD, password)
            .apply()
    }

    fun getUserDetails(): Pair<String, String>? {
        val email = preferences.value.getString(USER_EMAIL, null)
        val password = preferences.value.getString(USER_PASSWORD, null)
        return if (email != null && password != null) Pair(email, password) else null
    }

    fun checkAlreadyExist(email: String): Boolean {
        val registeredEmail = preferences.value.getString(USER_EMAIL, null)
        return registeredEmail == email
    }

    fun addToFavorite(movie: MovieDetail) {
        val favoriteMovies = getFavoriteMovies().toMutableList().apply {
            add(FavoriteMovie(movie.imdbId, movie.title, movie.poster))
        }
        saveFavoriteMovies(favoriteMovies)
    }

    fun getFavoriteMovies(): List<FavoriteMovie> {
        val gson = Gson()
        val json = preferences.value.getString(FAVOURITE_MOVIE, null)
        val type = object : TypeToken<List<FavoriteMovie>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun removeFromFavorites(imdbID: String) {
        val favoriteMovies = getFavoriteMovies().toMutableList().apply {
            removeAll { it.imdbID == imdbID }
        }
        saveFavoriteMovies(favoriteMovies)
    }

    private fun saveFavoriteMovies(favoriteMovies: List<FavoriteMovie>) {
        val gson = Gson()
        preferences.value.edit().putString(FAVOURITE_MOVIE, gson.toJson(favoriteMovies)).apply()
    }
}