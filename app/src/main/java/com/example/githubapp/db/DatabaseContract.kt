package com.example.githubapp.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USERNAME = "username"
            const val FULLNAME = "fullName"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val BIO = "bio"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"
            const val REPOSITORY = "repository"
            const val ISFAV = "isfav"
        }
    }
}