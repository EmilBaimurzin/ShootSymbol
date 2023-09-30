package com.shoot.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.shoot.game.R
import com.shoot.game.core.library.ViewBindingDialog
import com.shoot.game.databinding.DialogFinishBinding
import com.shoot.game.ui.other.MainActivity
import com.shoot.game.ui.scores.FragmentScores
import com.shoot.game.ui.shoot_game.FragmentShootGame

class DialogFinish : ViewBindingDialog<DialogFinishBinding>(DialogFinishBinding::inflate) {
    private val viewModel: FinishViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                (requireActivity() as MainActivity).navigateBack("main")
                dialog?.cancel()
                true
            } else {
                false
            }
        }
        binding.menu.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack("main")
            dialog?.cancel()
        }
        binding.scoreList.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentScores())
            dialog?.cancel()
        }
        binding.restart.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack("main")
            (requireActivity() as MainActivity).navigate(FragmentShootGame())
            dialog?.cancel()
        }
        val score = arguments?.getString("SCORE")
        binding.scores.text = score

        binding.stars.setImageResource(
            when (score!!.toInt()) {
                in 0..300 -> R.drawable.star04
                in 301..600 -> R.drawable.star03
                in 601..900 -> R.drawable.star02
                else -> R.drawable.star01
            }
        )

        viewModel.bestScore.observe(viewLifecycleOwner) {
            binding.bestScores.text = it.toString()
        }
    }
}