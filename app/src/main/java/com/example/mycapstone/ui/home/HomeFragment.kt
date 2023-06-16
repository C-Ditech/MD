package com.example.mycapstone.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.ArtikelActivity
import com.example.mycapstone.UserPreference
import com.example.mycapstone.ViewModelFactory
import com.example.mycapstone.databinding.FragmentHomeBinding

import com.example.mycapstone.ui.upload.UploadActivity
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var userPreference: UserPreference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(userPreference)).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getUserID().observe(viewLifecycleOwner) { userData ->
            val username = userData.name
            println("ini ada name sih : $username")
            binding.greetings.text = "Hi $username"
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        binding.date.text = currentDate

        binding.cekpenyakit.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            requireActivity().startActivity(intent)
        }


        binding.artikel1.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.putraperkasa.co.id/blog/koksidiosis-pada-ayam/"
            intent.putExtra("url", url)
            requireActivity().startActivity(intent)
        }

        binding.artikel2.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=&ved=2ahUKEwji0_DGlZf_AhVacGwGHZhcCoIQFnoECAwQAw&url=https%3A%2F%2Fwww.ceva.co.id%2FInformasi-Teknis%2FInformasi-lain%2FNewcastle-Disease-Penyakit-Paling-Merugikan%23%3A~%3Atext%3Ddi%2520industri%2520broiler.-%2CNewcastle%2520Disease%2520atau%2520yang%2520sering%2520disebut%2520ND%2520merupakan%2520salah%2520satu%2Cbiasanya%2520muncul%2520sebagai%2520penyakit%2520pernapasan.&usg=AOvVaw2NTHiPu--G7oDs58ytR5eW"
            intent.putExtra("url", url)
            requireActivity().startActivity(intent)
        }

        binding.artikel3.setOnClickListener {
            val intent = Intent(requireActivity(), ArtikelActivity::class.java)
            val url = "https://www.poultryindonesia.com/id/menilik-penyakit-salmonelosis-pada-peternakan-ayam/"
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