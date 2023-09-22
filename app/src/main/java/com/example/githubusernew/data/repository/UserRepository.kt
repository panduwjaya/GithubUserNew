package com.example.githubusernew.data.repository

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubusernew.data.local.FavoriteDao
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.data.remote.model.DetailResponse
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.data.remote.model.SearchUserResponse
import com.example.githubusernew.data.remote.network.ApiConfig
import com.example.githubusernew.utils.AppExecutors
import com.example.githubusernew.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val appExecutors: AppExecutors
){
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

    // MediatorLiveData
    private val result = MediatorLiveData<Result<List<FavoriteEntity>>>()

    fun getSearchUser(query: String): LiveData<Result<List<FavoriteEntity>>> {
        result.value = Result.Loading
        ApiConfig.getApiServices()
            .getSearch(query)
            .enqueue(object : Callback<SearchUserResponse> {
            // success
            override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                if (response.isSuccessful) {
                    // bagian remote data
                    val item = response.body()?.items
                    val userList = ArrayList<FavoriteEntity>()
                    // bagian local data
                    appExecutors.diskIO.execute {
                        item?.forEach { item ->
                            val isBookmarked = favoriteDao.isFavorited(item.id)
                            val user = FavoriteEntity(
                                item.id,
                                item.login,
                                item.avatarUrl,
                                item.htmlUrl,
                                isBookmarked
                            )
                            userList.add(user)
                        }
                        favoriteDao.deleteAll()
                        favoriteDao.insertUser(userList)
                    }
                }
            }
            // failed
            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        // local source
        // addSource digunakan apabila sumber data adalah LiveData
        val input = "%$query%"
        val localData: LiveData<List<FavoriteEntity>> = favoriteDao.getUser(input)
        result.addSource(localData) { newData: List<FavoriteEntity> ->
            result.value = Result.Success(newData)
        }
        return result
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
                        _listFollowers.value = response.body()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ArrayList<FollowUserResponseItem>>, t: Throwable) {
                    Log.e(TAG,"onFail ${t.message}")
                }

            })
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

    // favorite
    fun setFavoriteUser(user: FavoriteEntity, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorited = favoriteState
            favoriteDao.updateUser(user)
        }
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getFavoriteUser()
    }
}