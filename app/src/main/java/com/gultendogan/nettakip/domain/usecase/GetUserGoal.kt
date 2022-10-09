package com.gultendogan.nettakip.domain.usecase

import android.content.Context
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.uicomponents.MeasureUnit
import com.gultendogan.nettakip.utils.Constants
import com.gultendogan.nettakip.utils.extensions.orZero
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.abs

class GetUserGoal @Inject constructor(
    @ApplicationContext private val context: Context,
    private val netDao: NetDao
) {

    operator fun invoke(): String {
        val firstNet = netDao.fetchLastNet().firstOrNull()
        val goalNet = Hawk.get(Constants.Prefs.KEY_GOAL_NET, 0.0f)
        val difference = abs(firstNet?.value.orZero() - goalNet)
        val unit = MeasureUnit.findValue(Hawk.get<String>(Constants.Prefs.KEY_GOAL_NET_UNIT))
        return if (unit == MeasureUnit.TYT){
            String.format(context.getString(R.string.goal_summary_format),difference,context.getString(R.string.tyt))
        }else{
            String.format(context.getString(R.string.goal_summary_format),difference,context.getString(R.string.ayt))
        }
    }

}