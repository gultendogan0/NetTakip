package com.gultendogan.nettakip.ui.home.chart

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.utils.extensions.orZero

class NetValueFormatter(var histories: List<NetUIModel?>) : ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        val history = histories[barEntry?.x?.toInt().orZero]
        return history?.formattedValue.orEmpty()
    }

}