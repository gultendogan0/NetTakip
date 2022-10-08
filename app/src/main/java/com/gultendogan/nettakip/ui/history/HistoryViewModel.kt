package com.gultendogan.nettakip.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.domain.usecase.GetAllNets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private var getAllNets: GetAllNets) :
    ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        getNetHistories()
    }

    private fun getNetHistories() = viewModelScope.launch(Dispatchers.IO) {
        getAllNets().collectLatest { netHistories ->
            _uiState.update {
                it.copy(
                    histories = netHistories.reversed()
                )
            }
        }
    }

    data class UiState(
        var histories: List<NetUIModel?> = emptyList(),
    )

}