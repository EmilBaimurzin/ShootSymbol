package com.shoot.game.ui.menu

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.shoot.game.core.library.GameFragment
import com.shoot.game.databinding.FragmentMenuBinding
import com.shoot.game.ui.other.MainActivity
import com.shoot.game.ui.scores.FragmentScores
import com.shoot.game.ui.shoot_game.FragmentShootGame

class FragmentMenu : GameFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.play.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentShootGame())
        }

        binding.scores.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentScores())
        }

        binding.exit.setOnClickListener {
            requireActivity().finish()
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}