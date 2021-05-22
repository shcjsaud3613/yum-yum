package com.omnyom.yumyum.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dataList: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T)

    @Delete
    fun delete(data: T)
}