package com.gultendogan.nettakip.domain.usecase

import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.data.local.NetEntity
import com.gultendogan.nettakip.utils.extensions.endOfDay
import com.gultendogan.nettakip.utils.extensions.startOfDay
import java.util.*
import javax.inject.Inject

class SaveOrUpdateNet @Inject constructor(private val netDao: NetDao) {

    suspend operator fun invoke(net: String, note: String, emoji: String, date: Date) {
        val netList = netDao.fetchBy(
            startDate = date.startOfDay(),
            endDate = date.endOfDay()
        )
        if (netList.isEmpty()) {
            netDao.save(
                NetEntity(
                    timestamp = date,
                    value = net.toFloat(),
                    emoji = emoji,
                    note = note
                )
            )
        } else {
            netDao.update(
                NetEntity(
                    uid = netList.first().uid,
                    timestamp = date,
                    value = net.toFloat(),
                    emoji = emoji,
                    note = note
                )
            )
        }
    }

}