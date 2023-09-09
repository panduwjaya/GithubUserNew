package com.example.githubusernew.ui.follow

import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.repository.UserRepository

class FollowingsViewModel(private val userRepository: UserRepository): ViewModel() {

    fun setListFollowings(username: String) = userRepository.setListFollowings(username)

    fun getListFollowings() = userRepository.getFollowings()
}