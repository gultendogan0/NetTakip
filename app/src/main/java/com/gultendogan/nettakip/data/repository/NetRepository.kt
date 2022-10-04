package com.gultendogan.nettakip.data.repository

import com.gultendogan.nettakip.data.local.NetDao
import javax.inject.Inject

class NetRepository @Inject constructor(
    private val dbDao : NetDao
){
    fun getAllNets() =dbDao.getAllNets()
}