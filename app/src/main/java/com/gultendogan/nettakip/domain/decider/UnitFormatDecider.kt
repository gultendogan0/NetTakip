package com.gultendogan.nettakip.domain.decider

import android.content.Context
import com.orhanobut.hawk.Hawk
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.uicomponents.MeasureUnit
import com.gultendogan.nettakip.utils.Constants
import com.gultendogan.nettakip.utils.extensions.orZero
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UnitFormatDecider @Inject constructor(@ApplicationContext val context: Context){
    operator fun invoke(value: Float?): String {
        return if (MeasureUnit.findValue(Hawk.get(Constants.Prefs.KEY_GOAL_NET_UNIT)) == MeasureUnit.TYT) {
            context.getString(R.string.tyt_format, value.orZero())
        } else {
            context.getString(R.string.ayt_format, value.orZero())
        }
    }
}