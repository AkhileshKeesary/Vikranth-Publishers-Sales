package com.KAC.vikranthpublications.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.databinding.FragmentCompletedleadsBinding
import com.KAC.vikranthpublications.model.CompletedAdapter
import com.KAC.vikranthpublications.viewmodel.completedViewModelProvider
import com.KAC.vikranthpublications.viewmodel.CompletedleadsViewModel
import com.google.firebase.auth.FirebaseAuth

class CompletedleadsFragment : Fragment() {
    private var _binding : FragmentCompletedleadsBinding? = null
    private lateinit var completedleadsViewMode: CompletedleadsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CompletedAdapter
    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedleadsBinding.inflate(inflater,container,false)

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        completedleadsViewMode = ViewModelProvider(this,
            completedViewModelProvider(uid)
        ).get(CompletedleadsViewModel::class.java)

        val recyclerView = binding.compltedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = CompletedAdapter(arrayListOf())
        recyclerView.adapter = adapter

        completedleadsViewMode.tasks.observe(viewLifecycleOwner) { tasks ->
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