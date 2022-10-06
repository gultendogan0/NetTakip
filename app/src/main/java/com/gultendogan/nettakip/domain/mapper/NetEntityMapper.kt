package com.gultendogan.nettakip.domain.mapper

import com.gultendogan.nettakip.data.local.NetEntity
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.utils.extensions.orZero
import com.gultendogan.nettakip.utils.extensions.toFormat
import com.gultendogan.nettakip.domain.decider.DifferenceDecider
import com.gultendogan.nettakip.domain.decider.UnitFormatDecider
import java.util.*
import javax.inject.Inject

const val DATE_FORMAT_CHART = "dd MMM"

const val DEFAULT_VALUE_OF_NET_DIFFERENCE = 0.0f

class NetEntityMapper @Inject constructor(
    private val unitFormatDecider: UnitFormatDecider,
    private val differenceDecider: DifferenceDecider
) {
    fun map(entity: NetEntity?, previousEntity: NetEntity? = null): NetUIModel? {
        if (entity == null)
            return null

        val date = entity.timestamp ?: Date()
        val valueText =  entity.value?.toString().orEmpty()
        val emoji = entity.emoji.orEmpty()

        val difference: Float =
            differenceDecider.provideValue(current = entity.value, previous = previousEntity?.value)
        val differenceColor = differenceDecider.provideColor(difference)
        val differenceText = differenceDecider.provideText(difference)
        val valueWithUnit = unitFormatDecider.invoke(entity.value)

        return NetUIModel(
            uid = entity.uid.orZero,
            value = entity.value.orZero(),
            valueText = valueText,
            valueWithUnit = valueWithUnit,
            emoji = emoji,
            note = entity.note.orEmpty(),
            date = date,
            formattedDate = date.toFormat(DATE_FORMAT_CHART),
            formattedValue = "$emoji $valueText",
            difference = differenceText,
            differenceColor = differenceColor
        )
    }
}