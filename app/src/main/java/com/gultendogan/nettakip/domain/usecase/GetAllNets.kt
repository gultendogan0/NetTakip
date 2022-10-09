package com.gultendogan.nettakip.domain.usecase

import com.gultendogan.nettakip.data.local.NetEntity
import com.gultendogan.nettakip.data.repository.NetRepository
import com.gultendogan.nettakip.domain.mapper.NetEntityMapper
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllNets @Inject constructor(
    private val repository: NetRepository,
    private val mapper: NetEntityMapper
) {
    operator fun invoke() = repository.getAllNets().map { netList ->
        netList.mapIndexed { index, netEntity ->
            var previousEntity: NetEntity? = null
            val previousIndex = index + 1
            if (previousIndex < netList.size && previousIndex >= 0) {
                previousEntity = netList[previousIndex]
            }
            mapper.map(entity = netEntity, previousEntity = previousEntity)
        }.reversed()
    }
}