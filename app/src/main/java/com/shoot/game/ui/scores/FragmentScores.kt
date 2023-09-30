package com.shoot.game.ui.scores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoot.game.core.library.GameFragment
import com.shoot.game.databinding.FragmentScoresBinding
import com.shoot.game.domain.adapters.ScoresAdapter
import com.shoot.game.ui.other.MainActivity

class FragmentScores: GameFragment<FragmentScoresBinding>(FragmentScoresBinding::inflate) {
    private lateinit var scoresAdapter: ScoresAdapter
    private val viewModel: ScoresViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        binding.refresh.setOnClickListener {
            viewModel.refresh()
        }

        binding.home.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack("main")
        }

        viewModel.list.observe(viewLifecycleOwner) {
            scoresAdapter.items = it.toMutableList()
            scoresAdapter.notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        scoresAdapter = ScoresAdapter()
        with(binding.gameRv) {
            adapter = scoresAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
        }
    }
}