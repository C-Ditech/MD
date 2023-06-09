package com.example.mycapstone.ui.Akun

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.UserPreference
import com.example.mycapstone.ViewModelFactory
import com.example.mycapstone.databinding.FragmentAkunBinding
import com.example.mycapstone.databinding.FragmentHistoryBinding
import com.example.mycapstone.ui.history.HistoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private lateinit var viewModel: AkunViewModel
    private lateinit var userPreference: UserPreference



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi UserPreference
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        // Inisialisasi AkunViewModel dengan UserPreference
        viewModel = ViewModelProvider(this, ViewModelFactory(userPreference)).get(AkunViewModel::class.java)

        //logoutbutton
        binding.logoutButton.setOnClickListener({
            viewModel.logout()
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}