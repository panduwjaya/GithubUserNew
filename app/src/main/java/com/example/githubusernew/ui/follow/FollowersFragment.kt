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
import com.example.githubusernew.databinding.FragmentFollowersBinding
import com.example.githubusernew.ui.adapter.FollowAdapter
import com.example.githubusernew.ui.detailuser.DetailFragment
import com.example.githubusernew.utils.UserViewModelFactory
import java.util.Timer
import java.util.TimerTask

class FollowersFragment : Fragment() {

    // Handler
    private val handler = Handler(Looper.getMainLooper())

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: FollowersViewModel by viewModels {
        factory
    }

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<FollowUserResponseItem>()
    private var followersAdapter = FollowAdapter(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
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
                    getListFollower()
                }
            }
        }
        timer.schedule(timerTask, delay)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // receive data from detail fragment
        val dataUsername = arguments?.getString(DetailFragment.EXTRA_USERNAME)

        // setListFollower
        setListFollower(dataUsername!!)

        // recycler
        showRecycler()
    }

    fun showRecycler(){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = followersAdapter
    }

    private fun setListFollower(dataUsername: String) {
        viewModel.setListFollowers(dataUsername)
        showLoading(true)
    }

    private fun getListFollower() {
        viewModel.getListFollowers().observe(viewLifecycleOwner){result->
            if (result != null) {
                followersAdapter.setListUser(result)
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
            binding.pbFollower.visibility = View.VISIBLE
        } else {
            binding.pbFollower.visibility = View.GONE
        }
    }

}