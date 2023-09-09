package com.example.githubusernew.data.servicelocator

import android.content.Context
import com.example.githubusernew.data.local.FavoriteDatabase
import com.example.githubusernew.data.remote.network.ApiConfig
import com.example.githubusernew.data.repository.UserRepository
import com.example.githubusernew.utils.AppExecutors

object Inject {
    fun provideRepository(context: Context): UserRepository {
        // concurancy
        val appExecutors = AppExecutors()
        // database
        val database = FavoriteDatabase.getDatabase(context)
        val dao = database.favoriteDao()
        return UserRepository.getInstance(dao, appExecutors)
    }
}