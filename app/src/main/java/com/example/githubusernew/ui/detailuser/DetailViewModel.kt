package com.example.githubusernew.ui.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.data.remote.model.DetailResponse
import com.example.githubusernew.data.repository.UserRepository

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {

    fun setDetaiUser(login: String) = userRepository.setDetailUser(login)
    fun getDetailUser(): LiveData<DetailResponse>{
        return userRepository.listDetailUsers
    }

    fun saveNews(user: FavoriteEntity) {
        userRepository.setFavoriteUser(user, true)
    }
    fun deleteNews(user: FavoriteEntity) {
        userRepository.setFavoriteUser(user, false)
    }
}