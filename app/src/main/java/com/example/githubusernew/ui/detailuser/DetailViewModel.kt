package com.example.githubusernew.ui.detailuser

import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.repository.UserRepository

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {

    fun setDetaiUser(login: String) = userRepository.setDetailUser(login)

    fun getDetailUser() = userRepository.getDetailUser()

    fun checkFavorite(id: Int) = userRepository.checkFavorite(id)

    fun addFavorite(id: Int, login: String? = null,avatarUrl: String? = null, htmlUrl: String? = null) = userRepository.addFavorite(id,login,avatarUrl,htmlUrl)

    fun deleteFavorite(id: Int) = userRepository.deleteFavorite(id)
}