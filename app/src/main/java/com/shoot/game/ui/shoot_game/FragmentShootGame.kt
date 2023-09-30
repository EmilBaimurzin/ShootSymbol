package com.shoot.game.ui.shoot_game

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import com.shoot.game.R
import com.shoot.game.core.library.GameFragment
import com.shoot.game.databinding.FragmentShootGameBinding
import com.shoot.game.databinding.ItemSymbolBinding
import com.shoot.game.ui.dialogs.DialogFinish
import com.shoot.game.ui.other.MainActivity

class FragmentShootGame :
    GameFragment<FragmentShootGameBinding>(FragmentShootGameBinding::inflate) {
    private val viewModel: ShootGameViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTouchListener()

        startAction = {
            if (viewModel.sliderXY.value!!.y == 0f) {
                viewModel.setSliderXY(
                    xy.x / 2 - binding.slider.width / 2, binding.sliderBack.y, binding.sliderBack.x,
                    binding.sliderBack.x + binding.sliderBack.width
                )
            }
            if (viewModel.gameState) {
                viewModel.start(
                    (binding.sliderBack.y - binding.aim.height).toInt(),
                    dpToPx(100),
                    dpToPx(100),
                    (binding.sliderBack.y - dpToPx(83)).toInt(),
                    0,
                    xy.x.toInt() - dpToPx(70),
                    dpToPx(70),
                    dpToPx(83)
                )
            }
            binding.slider.y = binding.sliderBack.y
            binding.character.y = binding.sliderBack.y - binding.character.height
        }

        binding.home.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.restart.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
            (requireActivity() as MainActivity).navigate(FragmentShootGame())
        }

        binding.shot.setOnClickListener {
            viewModel.shoot(
                binding.aim.x.toInt(),
                binding.aim.y.toInt(),
                binding.aim.width,
                dpToPx(30),
                dpToPx(70)
            )
        }

        viewModel.symbols.observe(viewLifecycleOwner) {
            binding.symbolLayout.removeAllViews()
            it.forEach { symbol ->
                val symbolView = ItemSymbolBinding.inflate(
                    LayoutInflater.from(requireContext()),
                    binding.symbolLayout,
                    false
                )
                symbolView.symbol.setImageResource(
                    when (symbol.value) {
                        1 -> R.drawable.symbol01
                        2 -> R.drawable.symbol02
                        3 -> R.drawable.symbol03
                        4 -> R.drawable.symbol04
                        else -> R.drawable.symbol05
                    }
                )
                symbolView.time.text = "${symbol.time}"
                symbolView.time.setBackgroundResource(if (symbol.time <= 5) R.drawable.time02 else R.drawable.time)

                symbolView.root.x = symbol.x
                symbolView.root.y = symbol.y
                binding.symbolLayout.addView(symbolView.root)
            }
        }

        viewModel.lives.observe(viewLifecycleOwner) {
            binding.livesLayout.removeAllViews()
            repeat(it) {
                val liveView = ImageView(requireContext())
                liveView.layoutParams = LinearLayout.LayoutParams(dpToPx(25), dpToPx(25)).apply {
                    marginStart = dpToPx(2)
                    marginEnd = dpToPx(2)
                }
                liveView.setImageResource(R.drawable.life)
                binding.livesLayout.addView(liveView)
            }

            if (it == 0 && viewModel.gameState) {
                viewModel.gameState = false
                viewModel.stop()
                viewModel.addScore()
                (requireActivity() as MainActivity).navigateToDialog(DialogFinish().apply {
                    arguments = Bundle().apply {
                        putString("SCORE", viewModel.score.value.toString())
                    }
                })
            }
        }

        viewModel.score.observe(viewLifecycleOwner) {
            binding.scores.text = it.toString()
        }

        viewModel.aimY.observe(viewLifecycleOwner) {
            binding.aim.y = it
        }

        viewModel.sliderXY.observe(viewLifecycleOwner) {
            binding.slider.x = it.x
            binding.slider.y = binding.sliderBack.y
            binding.character.y = binding.sliderBack.y - binding.character.height
            binding.character.x = it.x - ((binding.character.width - binding.slider.width) / 2)
            binding.aim.x = it.x - ((binding.aim.width - binding.slider.width) / 2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener() {
        binding.sliderBack.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE) {
                viewModel.setSliderXY(
                    motionEvent.x - (binding.slider.width / 2),
                    binding.sliderBack.y,
                    binding.sliderBack.x,
                    binding.sliderBack.x + binding.sliderBack.width - binding.slider.width
                )
                true
            } else {
                false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }
}