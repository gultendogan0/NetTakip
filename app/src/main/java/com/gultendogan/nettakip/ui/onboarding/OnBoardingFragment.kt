package com.gultendogan.nettakip.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.databinding.FragmentOnBoardingBinding
import com.gultendogan.nettakip.uicomponents.CardRuler
import com.gultendogan.nettakip.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.gultendogan.nettakip.uicomponents.MeasureUnit
import android.widget.Toast
import com.gultendogan.nettakip.ui.onboarding.OnBoardingViewModel

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding by viewBinding(FragmentOnBoardingBinding::bind)

    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventsFlow.collect { event ->
                when (event) {
                    is OnBoardingViewModel.Event.Message -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }

                    is OnBoardingViewModel.Event.NavigateToHome -> {
                        findNavController().navigate(OnBoardingFragmentDirections.actionNavigateHome())
                    }
                }
            }
        }
    }


    private fun initViews() = with(binding) {
        cardRulerCurrent.render(CardRuler(unit = R.string.tyt, hint = R.string.enter_current_net))
        cardRulerGoal.render(CardRuler(unit = R.string.tyt, hint = R.string.enter_goal_net))

        btnContinue.setOnClickListener {
            val currentNet: Float = cardRulerCurrent.value
            val goalNet: Float = cardRulerGoal.value
            val unit = if (toggleButton.checkedButtonId == R.id.button1) {
                MeasureUnit.TYT
            } else {
                MeasureUnit.AYT
            }
            viewModel.save(
                currentNet = currentNet,
                goalNet = goalNet,
                unit = unit
            )
        }
        toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (!isChecked)
                return@addOnButtonCheckedListener
            if (checkedId == R.id.button1) {
                cardRulerCurrent.setUnit(MeasureUnit.TYT)
                cardRulerGoal.setUnit(MeasureUnit.AYT)
            } else {
                cardRulerCurrent.setUnit(MeasureUnit.AYT)
                cardRulerGoal.setUnit(MeasureUnit.AYT)
            }
        }
    }

}