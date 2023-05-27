package com.example.mycapstone.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.ArtikelActivity
import com.example.mycapstone.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        binding.date.text = currentDate




        binding.artikel1.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.dicoding.com"
            intent.putExtra("url", url)
            requireActivity().startActivity(intent)
        }

        binding.artikel2.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.dicoding.com"
            intent.putExtra("url", url)
            requireActivity().startActivity(intent)
        }

        binding.artikel3.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.dicoding.com"
            intent.putExtra("url", url)
            requireActivity().startActivity(intent)
        }

        return root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}