package com.gultendogan.nettakip.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.orhanobut.hawk.Hawk
import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.utils.Constants
import com.gultendogan.nettakip.utils.extensions.orZero
import com.gultendogan.nettakip.ui.home.chart.ChartType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.gultendogan.nettakip.domain.usecase.GetUserGoal
import com.gultendogan.nettakip.domain.usecase.GetAllNets
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var getAllNets: GetAllNets ,
    private val netDao: NetDao,
    private val getUserGoal: GetUserGoal
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchInsights()
    }


    private fun fetchInsights() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(netDao.getMax(),netDao.getMin(),netDao.getAvg()){ max, min, avg ->
                _uiState.update {
                    it.copy(
                        minNet = "$min",
                        maxNet = "$max",
                        averageNet = "$avg"
                    )
                }
            }.stateIn(this)
        }
    }

    fun fetchHome() = viewModelScope.launch(Dispatchers.IO) {
        getAllNets().collectLatest { netHistories ->
            _uiState.update {
                it.copy(
                    histories = netHistories,
                    startNet = "${netHistories.firstOrNull()?.formattedValue}",
                    shouldShowInsightView = netHistories.size > 1,
                    currentNet = "${netHistories.lastOrNull()?.formattedValue}",
                    reversedHistories = netHistories.asReversed().take(NET_LIMIT_FOR_HOME),
                    shouldShowAllNetButton = netHistories.size > NET_LIMIT_FOR_HOME,
                    barEntries = netHistories.mapIndexed { index, weight ->
                        BarEntry(index.toFloat(), weight?.value.orZero())
                    },
                    userGoal = getUserGoal(),
                    shouldShowLimitLine = Hawk.get(Constants.Prefs.KEY_CHART_LIMIT_LINE,true),
                    chartType =  ChartType.findValue(Hawk.get(Constants.Prefs.KEY_CHART_TYPE, 0)),
                    shouldShowEmptyView = netHistories.isEmpty(),
                    goalNet = "${Hawk.get(Constants.Prefs.KEY_GOAL_NET, 0.0)}"
                )
            }
        }
    }


    fun changeChartType(chartType: ChartType){
        val currentChartType=  ChartType.findValue(Hawk.get(Constants.Prefs.KEY_CHART_TYPE,0))
        if (chartType != currentChartType){
            Hawk.put(Constants.Prefs.KEY_CHART_TYPE,chartType.value)
            _uiState.update {
                it.copy(
                    chartType = chartType
                )
            }
        }
    }



    data class UiState(
        var maxNet: String? = null,
        var minNet: String? = null,
        var averageNet: String? = null,
        var startNet: String? = null,
        var currentNet: String? = null,
        var goalNet: String? = null,
        var histories: List<NetUIModel?> = emptyList(),
        var reversedHistories: List<NetUIModel?> = emptyList(),
        var barEntries: List<BarEntry> = emptyList(),
        var shouldShowEmptyView: Boolean = false,
        var shouldShowAllNetButton: Boolean = false,
        var shouldShowInsightView: Boolean = false,
        var shouldShowLimitLine : Boolean = false,
        var chartType: ChartType = ChartType.LINE,
        var userGoal: String ? = null
    )
    companion object {
        const val NET_LIMIT_FOR_HOME = 5
    }
}