package com.example.githubapp.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavUser (
    var username: String,
    var fullName: String,
    var avatar: String,
    var company: String,
    var location: String,
    var bio: String,
    var repository: String
) : Parcelable