package com.example.githubapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var username: String,
    var fullName: String,
    var avatar: String,
    var company: String,
    var location: String,
    var bio: String,
    var follower: String,
    var following: String,
    var repository: String
) : Parcelable