package com.gultendogan.nettakip.domain.usecase

import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.utils.extensions.endOfDay
import com.gultendogan.nettakip.utils.extensions.startOfDay
import java.util.*
import javax.inject.Inject

class DeleteNet @Inject constructor(private val netDao: NetDao) {

    suspend operator fun invoke(date: Date) {
        val netList = netDao.fetchBy(
            startDate = date.startOfDay(),
            endDate = date.endOfDay()
        )
        netList.firstOrNull()?.run {
            netDao.delete(this)
        }
    }

}