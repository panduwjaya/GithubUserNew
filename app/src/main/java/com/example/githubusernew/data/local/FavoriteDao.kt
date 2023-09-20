package com.example.githubusernew.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {

    // input data kedalam database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: List<FavoriteEntity>)

    // mengambil data
    @Query("SELECT * FROM favorite_user WHERE login LIKE :login")
    fun getUser(login: String): LiveData<List<FavoriteEntity>>

    // memperbarui data
    @Update
    fun updateUser(user: FavoriteEntity)

    @Query("SELECT * FROM favorite_user WHERE favorited = 1")
    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>

    @Query("DELETE FROM favorite_user WHERE favorited = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM favorite_user WHERE id = :id AND favorited = 1)")
    fun isFavorited(id: Int): Boolean

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    fun checkFavorited(id: Int): LiveData<List<FavoriteEntity>>
}