package com.gultendogan.nettakip.ui.home.chart

import com.github.mikephil.charting.formatter.ValueFormatter
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.utils.extensions.EMPTY

class XAxisValueDateFormatter(var histories: List<NetUIModel?>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value.toInt() < histories.size) {
            val history: NetUIModel? = histories[value.toInt()]
            return history?.formattedDate.orEmpty()
        }
        return String.EMPTY
    }
}