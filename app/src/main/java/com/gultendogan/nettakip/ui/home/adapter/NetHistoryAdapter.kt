package com.gultendogan.nettakip.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.base.BaseListAdapter
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.ui.home.adapter.NetHistoryViewHolder

class NetHistoryAdapter(private val onClickWeight: ((net: NetUIModel) -> Unit)?) :
    BaseListAdapter<NetUIModel>(
        itemsSame = { old, new -> old.uid == new.uid },
        contentsSame = { old, new -> old == new }
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_net_history, parent, false)
        return NetHistoryViewHolder(view, onClickWeight)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NetHistoryViewHolder -> {
                getItem(position)?.let { item -> holder.bind(item) }
            }
        }
    }
}