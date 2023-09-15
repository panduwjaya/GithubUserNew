package com.example.githubusernew.ui.detailuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.databinding.FragmentDetailBinding
import com.example.githubusernew.databinding.FragmentDetailFavoriteBinding
import com.example.githubusernew.ui.favoriteuser.FavoriteUserFragment
import com.example.githubusernew.ui.listuser.ListUserFragment
import com.example.githubusernew.utils.SectionsPagerAdapter
import com.example.githubusernew.utils.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragmentFavorite : Fragment() {

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: DetailViewModel by viewModels {
        factory
    }

    private var _binding: FragmentDetailFavoriteBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get data
        val dataLogin = arguments?.getString(FavoriteUserFragment.EXTRA_LOGIN)

        // set detailLayout
        if (dataLogin != null) {
            setDetailUser(dataLogin)
        }

        // get detailLayout
        getDetailUser()

        // sectionPagerAdapter
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity(), dataLogin)
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(DetailFragmentFavorite.TAB_TITLES[position])
        }.attach()

        (requireActivity() as AppCompatActivity).supportActionBar?.elevation = 0f
    }

    private fun setDetailUser(login: String) {
        viewModel.setDetaiUser(login)
        showLoading(true)
    }

    private fun getDetailUser() {
        viewModel.getDetailUser().observe(viewLifecycleOwner){result->
            if (result != null) {
                Glide.with(requireActivity())
                    .load(result.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfile)
                binding.tvUsername.text = result.login ?: "-"
                binding.tvName.text = result.name
                binding.tvUserCompany.text = result.company ?: "-"
                binding.tvFollower.text = "${result.followers} Followers"
                binding.tvFollowing.text = "${result.following} Following"
                binding.tvRepository.text = "${result.publicRepos} repo"
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}