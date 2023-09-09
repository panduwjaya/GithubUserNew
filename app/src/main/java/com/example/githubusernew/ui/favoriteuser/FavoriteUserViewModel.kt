package com.example.githubusernew.ui.favoriteuser

import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.repository.UserRepository

class FavoriteUserViewModel(private val userRepository: UserRepository): ViewModel(){
    fun getFavoriteUser() = userRepository.getFavoriteUser()
}