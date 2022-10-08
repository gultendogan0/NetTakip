package com.gultendogan.nettakip.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.databinding.FragmentHistoryBinding
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.ui.home.adapter.NetHistoryAdapter
import com.gultendogan.nettakip.ui.home.adapter.NetItemDecorator
import com.gultendogan.nettakip.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BottomSheetDialogFragment() {

    private val viewModel: HistoryViewModel by viewModels()

    private val binding by viewBinding(FragmentHistoryBinding::bind)

    private val adapterNetHistory: NetHistoryAdapter by lazy {
        NetHistoryAdapter(::onClickNet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {
        initNetRecyclerview()
    }

    private fun initNetRecyclerview() = with(binding.rvNetHistory) {
        adapter = adapterNetHistory
        addItemDecoration(NetItemDecorator(requireContext()))
        addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect(::setUIState)
        }
    }

    private fun setUIState(uiState: HistoryViewModel.UiState) = with(binding) {
        adapterNetHistory.submitList(uiState.histories)
    }

    private fun onClickNet(net: NetUIModel) {
        findNavController().navigate(HistoryFragmentDirections.actionNavigateAdd(net))
    }

}