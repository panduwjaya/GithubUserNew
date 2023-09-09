package com.example.githubusernew.data.remote.network

import com.example.githubusernew.data.remote.model.DetailResponse
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.data.remote.model.SearchUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices{

    @GET("search/users")
    fun getSearch(
        @Query("q") query: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<FollowUserResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<FollowUserResponseItem>>
}