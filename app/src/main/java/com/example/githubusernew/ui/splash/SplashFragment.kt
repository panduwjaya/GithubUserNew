package com.example.githubusernew.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.githubusernew.R
import com.example.githubusernew.darkmode.DarkSettingViewModel
import com.example.githubusernew.darkmode.SettingPreferences
import com.example.githubusernew.databinding.FragmentSplashBinding
import com.example.githubusernew.utils.DarkSettingViewModelFactory
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    // darkViewModel
    private lateinit var pref: SettingPreferences
    private lateinit var darkViewModel: DarkSettingViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SettingPreferences.getInstance(requireContext().dataStore)
        darkViewModel = ViewModelProvider(requireActivity(), DarkSettingViewModelFactory(pref)).get(
            DarkSettingViewModel::class.java
        )

        darkViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        lifecycleScope.launchWhenResumed {
            delay(2000) // Tambahkan delay selama 2 detik (atau sesuai dengan kebutuhan Anda)
            findNavController().navigate(R.id.action_splashFragment_to_listUserFragment)
        }

    }
}