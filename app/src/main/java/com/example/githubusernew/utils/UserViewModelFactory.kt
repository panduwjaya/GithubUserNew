package com.example.githubusernew.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernew.data.repository.UserRepository
import com.example.githubusernew.data.servicelocator.Inject
import com.example.githubusernew.ui.detailuser.DetailViewModel
import com.example.githubusernew.ui.favoriteuser.FavoriteUserViewModel
import com.example.githubusernew.ui.follow.FollowersViewModel
import com.example.githubusernew.ui.follow.FollowingsViewModel
import com.example.githubusernew.ui.listuser.ListUserViewModel

class UserViewModelFactory private constructor(private val userRepository: UserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListUserViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(FollowersViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(FollowingsViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UserViewModelFactory(Inject.provideRepository(context))
            }.also { instance = it }
    }
}