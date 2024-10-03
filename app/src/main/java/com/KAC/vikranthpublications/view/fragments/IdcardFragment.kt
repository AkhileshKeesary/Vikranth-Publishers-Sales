package com.KAC.vikranthpublications.view.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.KAC.vikranthpublications.databinding.FragmentIdcardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class IdcardFragment : Fragment() {
    private var _binding : FragmentIdcardBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentIdcardBinding.inflate(inflater,container,false)

        val imageView = binding.idcardImageView

        val storageReference = Firebase.storage.reference

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User Details").child(uid)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fn = dataSnapshot.child("full_name").getValue(String::class.java)
                Log.e("Full Name", "Loaded $fn")

                // Include both the folder path and the file name
                val imageRef = storageReference.child("idcardData/$fn/$fn.PNG")
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(imageView)
                }.addOnFailureListener {
                    Log.e("Image Loading", "Failed to load image", it)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })




        return binding.root
    }


}