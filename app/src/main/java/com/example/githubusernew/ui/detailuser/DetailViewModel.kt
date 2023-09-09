package com.example.githubusernew.ui.detailuser

import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.repository.UserRepository

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {

    fun setDetaiUser(login: String) = userRepository.setDetailUser(login)

    fun getDetailUser() = userRepository.getDetailUser()

    fun checkFavorite(id: Int) = userRepository.checkFavorite(id)

    fun addFavorite(id: Int, login: String,avatarUrl: String, html_url: String) = userRepository.addFavorite(id,login,avatarUrl,html_url)

    fun deleteFavorite(id: Int) = userRepository.deleteFavorite(id)
}