package com.example.wackamoleadvanced.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY score DESC, createdAt ASC LIMIT :limit")
    suspend fun getTopScores(limit: Int): List<ScoreEntity>

    @Query("SELECT * FROM scores WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserScores(userId: Long): List<ScoreEntity>
}
