package com.KAC.vikranthpublications.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.KAC.vikranthpublications.databinding.FragmentHomeBinding
import com.KAC.vikranthpublications.model.UpcommingAdapter
import com.KAC.vikranthpublications.viewmodel.HomeViewModel
import com.KAC.vikranthpublications.viewmodel.UpcommingViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpcommingAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize ViewModel with UID
        homeViewModel = ViewModelProvider(this, UpcommingViewModelProvider(uid)).get(HomeViewModel::class.java)

        // Initialize RecyclerView
        val recyclerView = binding.upcommingRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UpcommingAdapter(arrayListOf())
        recyclerView.adapter = adapter

        // Observe ViewModel data
        homeViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks?.let {
                adapter.updateData(it)
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}