package com.KAC.vikranthpublications.view.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.FragmentOutdatedBinding
import com.KAC.vikranthpublications.model.OutDatedAdapter
import com.KAC.vikranthpublications.model.UpcommingAdapter
import com.KAC.vikranthpublications.viewmodel.HomeViewModel
import com.KAC.vikranthpublications.viewmodel.OutdatedViewModel
import com.KAC.vikranthpublications.viewmodel.UpcommingViewModelProvider
import com.KAC.vikranthpublications.viewmodel.outdatedViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class OutdatedFragment : Fragment() {
    private var _binding:FragmentOutdatedBinding? = null
    private lateinit var outdatedViewModel : OutdatedViewModel
    private lateinit var adapter: OutDatedAdapter
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOutdatedBinding.inflate(inflater,container,false)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        outdatedViewModel = ViewModelProvider(this, outdatedViewModelProvider(uid)).get(OutdatedViewModel::class.java)

        // Initialize RecyclerView
        val recyclerView = binding.outdatedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = OutDatedAdapter(arrayListOf())
        recyclerView.adapter = adapter

        // Observe ViewModel data
        outdatedViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks?.let {
                adapter.updateData(it)
            }
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}