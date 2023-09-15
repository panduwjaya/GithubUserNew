package com.example.githubusernew.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteEntity(
    @PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "login")
    val login: String? = null,
    @field:ColumnInfo(name = "avatar_url")
    val avatar_url: String? = null,
    @field:ColumnInfo(name = "html_url")
    val html_url: String? = null,
    @field:ColumnInfo(name = "favorited")
    var isFavorited: Boolean
): Parcelable