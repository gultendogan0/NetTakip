package com.gultendogan.nettakip.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import com.google.android.material.card.MaterialCardView
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.databinding.ViewCardRulerBinding

enum class MeasureUnit(var value: String) {
    TYT("tyt"),
    AYT("ayt");
    companion object {
        fun findValue(value: String?): MeasureUnit = values().find { it.value == value } ?: TYT
    }
}

const val FLOOR_FOR_AYT_TO_TYT = 1f

class CardRulerViewComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {
    private val binding: ViewCardRulerBinding by lazy {
        ViewCardRulerBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private var shouldChangeRulerView = true

    var value: Float = 0.0f

    var currentUnit = MeasureUnit.TYT

    fun setUnit(unit: MeasureUnit){
        if (unit == MeasureUnit.AYT){
            if (currentUnit == MeasureUnit.TYT){
                value *= FLOOR_FOR_AYT_TO_TYT
            }
            binding.rulerViewCurrent.setUnitStr(context.getString(R.string.ayt))
        }else{
            if (currentUnit == MeasureUnit.AYT){
                value /= FLOOR_FOR_AYT_TO_TYT
            }
            binding.rulerViewCurrent.setUnitStr(context.getString(R.string.tyt))
        }
        currentUnit = unit
        binding.tilInputCurrentNet.setText(context.getString(R.string.tyt_format, value))
        binding.rulerViewCurrent.setValue(value)
    }

    fun render(cardRuler: CardRuler) = with(binding) {
        val context = binding.root.context
        rulerViewCurrent.setUnitStr(context.getString(cardRuler.unit))
        tfInputCurrentNet.setHint(cardRuler.hint)
        rulerViewCurrent.setValueListener {
            shouldChangeRulerView = false
            value = it
            tilInputCurrentNet.setText(context.getString(R.string.tyt_format, it))
        }
        rulerViewCurrent.setMaxValue(cardRuler.max)
        rulerViewCurrent.setValue(cardRuler.num)
        tilInputCurrentNet.addTextChangedListener {
            if (shouldChangeRulerView) {
                val net = it.toString().trim().toFloatOrNull()
                net?.run {
                    rulerViewCurrent.setValue(this)
                    value = this
                }
            }
            shouldChangeRulerView = true
        }
    }
}
data class CardRuler(
    @StringRes var unit: Int,
    @StringRes var hint: Int,
    var num: Float = 75.0f,
    var max: Int = 350
)