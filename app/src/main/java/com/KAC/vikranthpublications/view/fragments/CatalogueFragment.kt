package com.KAC.vikranthpublications.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.KAC.vikranthpublications.R
import com.KAC.vikranthpublications.databinding.FragmentCatalogueBinding


class CatalogueFragment : Fragment() {
    private var _binding :FragmentCatalogueBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
       _binding = FragmentCatalogueBinding.inflate(inflater,container,false)




        return binding.root
    }


}