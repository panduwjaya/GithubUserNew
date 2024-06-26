package com.example.githubusernew.ui.listuser

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.darkmode.DarkSettingViewModel
import com.example.githubusernew.darkmode.SettingPreferences
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.databinding.FragmentListUserBinding
import com.example.githubusernew.ui.adapter.SearchUserAdapter
import com.example.githubusernew.utils.DarkSettingViewModelFactory
import com.example.githubusernew.utils.Result
import com.example.githubusernew.utils.UserViewModelFactory
import java.util.Timer
import java.util.TimerTask

class ListUserFragment : Fragment() {

    // Handler
    private val handler = Handler(Looper.getMainLooper())

    // viewModel
    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: ListUserViewModel by viewModels {
        factory
    }

    // darkViewModel
    private lateinit var pref: SettingPreferences
    private lateinit var darkViewModel: DarkSettingViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private var _binding: FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<FavoriteEntity>()
    private val listAdapter = SearchUserAdapter(list)

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_PARCEL = "extra_parcel"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.myEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding?.pbListUser?.visibility = View.VISIBLE

                val timer = Timer()
                val delay: Long = 1000

                val timerTask = object : TimerTask() {
                    override fun run() {
                        handler.post {
                            findUser()
                        }
                    }
                }
                timer.schedule(timerTask, delay)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SettingPreferences.getInstance(requireContext().dataStore)
        darkViewModel = ViewModelProvider(requireActivity(), DarkSettingViewModelFactory(pref)).get(
            DarkSettingViewModel::class.java
        )

        darkViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setHasOptionsMenu(true)

        // fab
        binding.fabFavorite.setOnClickListener {
            view.findNavController().navigate(R.id.action_listUserFragment_to_favoriteUserFragment)
        }

        listAdapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteEntity) {
                val mBundle = Bundle()
                mBundle.putString(EXTRA_LOGIN, data.login)
                mBundle.putParcelable(EXTRA_PARCEL,data)
                view.findNavController().navigate(R.id.action_listUserFragment_to_detailFragment, mBundle)
            }
        })

        // adapter
        showRecycler()
    }

    fun showRecycler() {
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = listAdapter
    }

    private fun findUser() {
        val query = binding.myEditText.text.toString()
        if (query.isEmpty()) return
        viewModel.getSearchUser(query).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.pbListUser?.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding?.pbListUser?.visibility = View.GONE
                        val userData = result.data
                        listAdapter.setListUser(userData)
                    }

                    is Result.Error -> {
                        binding?.pbListUser?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu -> {
                findNavController().navigate(R.id.action_listUserFragment_to_darkSettingFragment)
                true
            }

            R.id.profile_menu -> {
                findNavController().navigate(R.id.action_listUserFragment_to_profileFragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}