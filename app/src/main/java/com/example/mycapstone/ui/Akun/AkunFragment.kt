package com.example.mycapstone.ui.Akun

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mycapstone.*
import com.example.mycapstone.databinding.FragmentAkunBinding

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

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(userPreference)).get(AkunViewModel::class.java)


        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }


        viewModel.getUserData().observe(viewLifecycleOwner) { userData ->
            val tokken = userData.token
            println("ini ada token sih : $tokken")
        }

        viewModel.getUserID().observe(viewLifecycleOwner) { userData ->
            val username = userData.name
            println("ini ada name sih : $username")
            binding.textUsername.text = username
        }

        viewModel.getEmail().observe(viewLifecycleOwner) { userData ->
            val email = userData.email
            println("ini ada email sih : $email")
            binding.textEmail.text = email
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLogoutConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton("Batal", null)
            .create()
        dialog.show()
    }
}