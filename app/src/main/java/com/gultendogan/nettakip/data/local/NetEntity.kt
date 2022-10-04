package com.gultendogan.nettakip.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "net")
data class NetEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Date?,
    @ColumnInfo(name = "value") val value: Float?,
    @ColumnInfo(name="emoji") val emoji:String?,
    @ColumnInfo(name="note") val note:String
)