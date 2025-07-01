package com.example.binlookup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.binlookup.databinding.HistoryBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet : BottomSheetDialogFragment() {
    val viewModel: BinViewModel by activityViewModels()
    lateinit var binding: HistoryBottomsheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.history_bottomsheet, container, false)
        viewModel.history.observe(viewLifecycleOwner) {
            Log.d("TAG", "onCreateView: ishlavotdi bottomshet")
            if (it.isNotEmpty() || it !=null) {
                binding.listOfHistory.layoutManager = LinearLayoutManager(context)
                binding.listOfHistory.adapter = HistoryAdapter(requireContext(),it)
            } else {
                Toast.makeText(context, "History list is empty", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}