package com.example.githubusernew.data.repository

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubusernew.data.local.FavoriteDao
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.data.remote.model.DetailResponse
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.data.remote.model.ItemsItem
import com.example.githubusernew.data.remote.model.SearchUserResponse
import com.example.githubusernew.data.remote.network.ApiConfig
import com.example.githubusernew.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val appExecutors: AppExecutors
){
    // make single instance
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            favoriteDao: FavoriteDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(favoriteDao, appExecutors)
            }.also { instance = it }
    }

    // search user
    private val _listUsers = MutableLiveData<ArrayList<ItemsItem>>()
    val listUsers: LiveData<ArrayList<ItemsItem>> = _listUsers

    fun setSearchUser(query: String){
        ApiConfig.getApiServices()
            .getSearch(query)
            .enqueue(object : Callback<SearchUserResponse> {
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>
                ) {
                    if (response.isSuccessful){
                        _listUsers.value = response.body()?.items
                    }else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
    }

    fun getSearchUsers(): LiveData<ArrayList<ItemsItem>> {
        return listUsers
    }

    // detail fragment
    private val _listDetailUsers = MutableLiveData<DetailResponse>()
    val listDetailUsers: LiveData<DetailResponse> = _listDetailUsers

    fun setDetailUser(login: String){
        ApiConfig.getApiServices()
            .getDetail(login)
            .enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailUsers.value = response.body()
                    }else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
    }

    fun getDetailUser(): LiveData<DetailResponse>{
        return listDetailUsers
    }


    // follower fragment
    private var _listFollowers = MutableLiveData<ArrayList<FollowUserResponseItem>?>()
    val listFollowers: LiveData<ArrayList<FollowUserResponseItem>?> = _listFollowers

    fun setListFollowers(username: String){
        ApiConfig.getApiServices()
            .getFollower(username)
            .enqueue(object: Callback<ArrayList<FollowUserResponseItem>>{
                override fun onResponse(
                    call: Call<ArrayList<FollowUserResponseItem>>,
                    response: Response<ArrayList<FollowUserResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val followUserResponse = response.body()
                        _listFollowers.value = followUserResponse
                    } else {
                        // Tangani kesalahan jika respons tidak berhasil
                    }
                }

                override fun onFailure(call: Call<ArrayList<FollowUserResponseItem>>, t: Throwable) {
                    Log.e(TAG,"onFail ${t.message}")
                }

            })
    }

    fun getFollowers(): LiveData<ArrayList<FollowUserResponseItem>?> {
        return listFollowers
    }

    // following fragment
    private val _listFollowing = MutableLiveData<ArrayList<FollowUserResponseItem>?>()
    val listFollowing: LiveData<ArrayList<FollowUserResponseItem>?> = _listFollowing

    // setListFollowings getListFollowings
    fun setListFollowings(username: String){
        ApiConfig.getApiServices()
            .getFollowing(username)
            .enqueue(object: Callback<ArrayList<FollowUserResponseItem>>{
                override fun onResponse(
                    call: Call<ArrayList<FollowUserResponseItem>>,
                    response: Response<ArrayList<FollowUserResponseItem>>
                ) {
                    if(response.isSuccessful){
                        _listFollowing.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ArrayList<FollowUserResponseItem>>, t: Throwable) {
                    Log.e(TAG,"onFail ${t.message}")
                }


            })
    }

    fun getFollowings(): LiveData<ArrayList<FollowUserResponseItem>?> {
        return listFollowing
    }

    // favorite
    fun checkFavorite(id: Int) = favoriteDao.checkFavorite(id)

    fun addFavorite(id: Int, login: String,avatarUrl: String, html_url: String){
        val userList = ArrayList<FavoriteEntity>()
        appExecutors.diskIO.execute {
            var user = FavoriteEntity(
                id,
                login,
                avatarUrl,
                html_url
            )
            userList.add(user)
        }
        favoriteDao.insertFavorite(userList)
    }

    fun deleteFavorite(id: Int){
        appExecutors.diskIO.execute {
            favoriteDao.deleteAll(id)
        }
    }

    // favorite fragment
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getFavoriteUser()
    }
}