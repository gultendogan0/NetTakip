package com.gultendogan.nettakip.ui.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.gultendogan.nettakip.databinding.ItemNetHistoryBinding
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.utils.extensions.toFormat

const val DATE_FORMAT = "dd MMM yyyy"

class NetHistoryViewHolder(
    view: View,
    private val onClickWeight: ((net: NetUIModel) -> Unit)?
) :
    RecyclerView.ViewHolder(view) {
    private val binding = ItemNetHistoryBinding.bind(view)
    fun bind(uiModel: NetUIModel) = with(binding) {
        tvNote.text = uiModel.note
        tvEmoji.text = uiModel.emoji
        tvNote.isGone = uiModel.note.isBlank()
        tvDate.text = uiModel.date.toFormat(DATE_FORMAT)
        tvNet.text = uiModel.valueWithUnit
        tvDifference.text = uiModel.difference
        tvDifference.setTextColor(ContextCompat.getColor(binding.root.context,uiModel.differenceColor))
        itemView.setOnClickListener {
            onClickWeight?.invoke(uiModel)
        }
    }
}