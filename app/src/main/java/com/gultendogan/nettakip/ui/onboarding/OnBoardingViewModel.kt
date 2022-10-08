package com.gultendogan.nettakip.ui.onboarding

import androidx.annotation.StringRes
import com.gultendogan.nettakip.utils.extensions.ZERO
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gultendogan.nettakip.R
import com.orhanobut.hawk.Hawk
import com.gultendogan.nettakip.domain.usecase.SaveOrUpdateNet
import com.gultendogan.nettakip.utils.Constants
import com.gultendogan.nettakip.utils.extensions.EMPTY
import com.gultendogan.nettakip.uicomponents.MeasureUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val saveOrUpdateNet: SaveOrUpdateNet
) : ViewModel() {

    sealed class Event {
        object NavigateToHome : Event()
        data class Message(@StringRes var message: Int) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun sendEvent(event: Event){
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun save(currentNet: Float, goalNet: Float, unit: MeasureUnit) {
        if (currentNet == goalNet) {
            sendEvent(Event.Message(R.string.alert_current_net_must_different_with_goal_net))
            return
        }
        if (currentNet.toInt() == Int.ZERO) {
            sendEvent(Event.Message(R.string.alert_net_bigger_than_zero))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            Hawk.put(Constants.Prefs.KEY_GOAL_NET, goalNet)
            Hawk.put(Constants.Prefs.KEY_GOAL_NET_UNIT, MeasureUnit.findValue(unit.value).value)
            Hawk.put(Constants.Prefs.KEY_GOAL_NET_DATE, Date().time)
            Hawk.put(Constants.Prefs.KEY_SHOULD_SHOW_ON_BOARDING, false)
            saveOrUpdateNet.invoke("$currentNet", String.EMPTY, String.EMPTY, Date())
            eventChannel.send(Event.NavigateToHome)
        }
    }

}