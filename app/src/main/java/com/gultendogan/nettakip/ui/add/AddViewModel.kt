package com.gultendogan.nettakip.ui.add

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.domain.usecase.DeleteNet
import com.gultendogan.nettakip.domain.mapper.NetEntityMapper
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.domain.usecase.SaveOrUpdateNet
import com.gultendogan.nettakip.utils.extensions.endOfDay
import com.gultendogan.nettakip.utils.extensions.startOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val netDao: NetDao,
    private val saveOrUpdateNet: SaveOrUpdateNet,
    private val deleteNet: DeleteNet,
    private val mapper: NetEntityMapper
) : ViewModel() {

    sealed class Event {
        object PopBackStack : Event()
        data class ShowToast(@StringRes val textResId: Int) : Event()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun delete(date: Date){
        viewModelScope.launch(Dispatchers.IO) {
            deleteNet.invoke(date)
        }
    }

    fun saveOrUpdateNet(net: String, note: String, emoji: String, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {

            when {
                net.isBlank() -> {
                    eventChannel.send(Event.ShowToast(R.string.alert_blank_net))
                }
                else -> {
                    saveOrUpdateNet.invoke(
                        net = net,
                        note = note,
                        emoji = emoji,
                        date = date
                    )
                    eventChannel.send(Event.PopBackStack)
                }
            }
        }
    }

    fun fetchDate(date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            val netList = netDao.fetchBy(
                startDate = date.startOfDay(),
                endDate = date.endOfDay()
            )
            val uiModel = mapper.map(netList.firstOrNull())
            _uiState.update {
                it.copy(currentNet = uiModel)
            }
        }
    }

    data class UiState(
        var currentNet: NetUIModel? = null
    )

}