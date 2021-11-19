package com.example.githubapp.helper

import android.database.Cursor
import com.example.githubapp.db.DatabaseContract
import com.example.githubapp.entity.FavUser
import com.example.githubapp.entity.User
import java.lang.reflect.Array.getBoolean

object MappingHelper {
    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()

        usersCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val fullname = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FULLNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val bio = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.BIO))
                val follower = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWER))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
                val isFav : Boolean = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ISFAV)) > 0
                notesList.add(User(username, fullname, avatar, company, location, bio,follower, following, repository, isFav))
            }
        }
        return notesList
    }
}