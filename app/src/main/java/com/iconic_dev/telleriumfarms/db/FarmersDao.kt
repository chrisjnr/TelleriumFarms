package com.iconic_dev.telleriumfarms.db

import androidx.paging.DataSource
import androidx.room.*
import com.iconic_dev.telleriumfarms.db.models.Farmer

/**
 * Created by manuelchris-ogar on 24/10/2020.
 */
@Dao
interface FarmersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(farmer: Farmer)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(farmer: Farmer)


    @Query("Delete from farmer")
    suspend fun deleteAll()

    @Query("Select * from farmer")
    fun getAll(): DataSource.Factory<Int, Farmer>

    @Query("Select * from farmer where id like :search")
    fun getShopsDataSearch(search: String?): List<Farmer?>?
}