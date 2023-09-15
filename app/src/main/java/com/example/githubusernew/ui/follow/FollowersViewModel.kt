package com.example.githubusernew.ui.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.data.repository.UserRepository

class FollowersViewModel(private val userRepository: UserRepository): ViewModel() {
    fun setListFollowers(username: String) = userRepository.setListFollowers(username)

    fun getListFollowers(): LiveData<ArrayList<FollowUserResponseItem>?>{
        return userRepository.listFollowers
    }
}