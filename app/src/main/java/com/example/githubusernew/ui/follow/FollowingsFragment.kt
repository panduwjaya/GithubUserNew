package com.example.githubusernew.ui.follow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.data.remote.model.FollowUserResponseItem
import com.example.githubusernew.databinding.FragmentFollowingsBinding
import com.example.githubusernew.ui.adapter.FollowAdapter
import com.example.githubusernew.ui.detailuser.DetailFragment
import com.example.githubusernew.utils.UserViewModelFactory
import java.util.Timer
import java.util.TimerTask

class FollowingsFragment : Fragment() {

    // Handler
    private val handler = Handler(Looper.getMainLooper())

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: FollowingsViewModel by viewModels {
        factory
    }

    private var _binding: FragmentFollowingsBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<FollowUserResponseItem>()
    private val followingsAdapter = FollowAdapter(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showLoading(true)

        val timer = Timer()
        val delay: Long = 500

        val timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    getListFollowing()
                }
            }
        }
        timer.schedule(timerTask, delay)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataUsername = arguments?.getString(DetailFragment.EXTRA_USERNAME)

        // setListFollower
        setListFollowing(dataUsername!!)

        // recycler
        showRecycler()
    }

    fun showRecycler(){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = followingsAdapter
    }

    private fun setListFollowing(dataUsername: String) {
        viewModel.setListFollowings(dataUsername)
        showLoading(true)
    }

    private fun getListFollowing() {
        viewModel.getListFollowings().observe(viewLifecycleOwner){result->
            if (result != null) {
                followingsAdapter.setListUser(result)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbFollowing.visibility = View.VISIBLE
        } else {
            binding.pbFollowing.visibility = View.GONE
        }
    }
}