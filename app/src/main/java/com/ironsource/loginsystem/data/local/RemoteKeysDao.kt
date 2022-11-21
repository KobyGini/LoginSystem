package com.ironsource.loginsystem.data.local

import androidx.room.*

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM REMOTE_KEYS_TABLE WHERE id = :id")
    suspend fun remoteKeysMovieId(id: String): RemoteKeys

    @Query("DELETE FROM REMOTE_KEYS_TABLE")
    suspend fun clearRemoteKeys()
}