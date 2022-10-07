package com.gultendogan.nettakip.ui.add

import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.gultendogan.nettakip.R
import com.gultendogan.nettakip.databinding.FragmentAddBinding
import com.gultendogan.nettakip.domain.uimodel.NetUIModel
import com.gultendogan.nettakip.ui.emoji.EmojiFragment
import com.gultendogan.nettakip.utils.extensions.*
import com.gultendogan.nettakip.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

const val CURRENT_DATE_FORMAT = "dd MMM yyyy"
const val TAG_DATE_PICKER = "Tag_Date_Picker"

@AndroidEntryPoint
class AddFragment : BottomSheetDialogFragment(){
    private val args: AddFragmentArgs by navArgs()
    private val viewModel: AddViewModel by viewModels()
    private var selectedDate = Date()
    private var emoji: String = String.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add, container, false)

    private val binding by viewBinding(FragmentAddBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
        val argNet = args.net
        if (argNet != null){
            fetchDate(argNet.date)
        }else{
            binding.btnNext.isGone = true
            viewModel.fetchDate(selectedDate)
        }
    }

    private fun initViews() = with(binding) {

        args.net?.date?.run(::fetchDate)

        btnPrev.setOnClickListener {
            fetchDate(selectedDate.prevDay())
        }

        btnNext.setOnClickListener {
            fetchDate(selectedDate.nextDay())
        }

        btnEmoji.setOnClickListener {
            findNavController().navigate(R.id.action_navigate_emoji)
        }

        btnDelete.setOnClickListener {
            viewModel.delete(date = selectedDate)
            findNavController().popBackStack()
        }

        btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val startFrom = calendar.timeInMillis

            val constraints = CalendarConstraints.Builder()
                .setEnd(startFrom)
                .setValidator(DateValidatorPointBackward.now())
                .build()

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_date))
                    .setCalendarConstraints(constraints)
                    .setSelection(selectedDate.time)
                    .build()

            datePicker.addOnPositiveButtonClickListener { timestamp ->

                fetchDate(Date(timestamp))
            }

            datePicker.show(parentFragmentManager, TAG_DATE_PICKER);
        }

        btnSelectDate.text = selectedDate.toFormat(CURRENT_DATE_FORMAT)

        btnSaveOrUpdate.setOnClickListener {
            val net = tilInputNet.text.toString()
            val note = tilInputNote.text.toString()
            viewModel.saveOrUpdateNet(
                net = net,
                note = note,
                emoji = emoji,
                date = selectedDate
            )
        }
    }

    private fun fetchDate(date: Date){
        emoji = String.EMPTY
        selectedDate = date
        binding.btnSelectDate.text = selectedDate.toFormat(CURRENT_DATE_FORMAT)
        viewModel.fetchDate(selectedDate)

        val shouldHideNextButton =
            selectedDate > Date() || selectedDate.toFormat(CURRENT_DATE_FORMAT) == Date().toFormat(
                CURRENT_DATE_FORMAT
            )

        binding.btnNext.isGone = shouldHideNextButton
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventsFlow.collect { event ->
                when (event) {
                    AddViewModel.Event.PopBackStack -> {
                        findNavController().popBackStack()
                    }
                    is AddViewModel.Event.ShowToast -> {
                        context.showToast(event.textResId)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect(::setUIState)
        }

        setFragmentResultListener(EmojiFragment.KEY_REQUEST_EMOJI) { _, bundle ->
            emoji = bundle.getString(EmojiFragment.KEY_BUNDLE_EMOJI).orEmpty()
            binding.btnEmoji.text =  getString(R.string.select_emoji_with_emoji_format, emoji)
        }
    }


    private fun setUIState(uiState: AddViewModel.UiState) = with(binding) {
        val net = uiState.currentNet
        tilInputNote.setText(net?.note.orEmpty())
        tilInputNet.setText(uiState.currentNet?.valueText.orEmpty())
        setBtnSaveStatus(net = net)
        setBtnEmojiStatus(net = net)
        setDeleteButton(net = net)
    }

    private fun setBtnEmojiStatus(net: NetUIModel?) = with(binding.btnEmoji) {
        if (net == null) {
            setText(R.string.select_emoji)
        } else {
            emoji = net.emoji
            text = getString(R.string.select_emoji_with_emoji_format, net.emoji)
        }
    }

    private fun setDeleteButton(net: NetUIModel?){
        binding.btnDelete.isGone = net == null
    }

    private fun setBtnSaveStatus(net: NetUIModel?) = with(binding.btnSaveOrUpdate) {
        if (net == null) {
            setText(R.string.save)
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_add_24)
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_700))


        } else {
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_edit_24)
            setText(R.string.update)
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

}