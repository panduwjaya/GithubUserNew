package com.example.githubusernew.ui.listuser

import androidx.lifecycle.ViewModel
import com.example.githubusernew.data.repository.UserRepository


class ListUserViewModel(private val userRepository: UserRepository): ViewModel() {

    fun setSearchUser(query: String) = userRepository.setSearchUser(query)

    fun getSearchUser() = userRepository.getSearchUsers()
}