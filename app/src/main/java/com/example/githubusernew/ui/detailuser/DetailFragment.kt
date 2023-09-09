package com.example.githubusernew.ui.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.githubusernew.R
import com.example.githubusernew.databinding.FragmentDetailBinding
import com.example.githubusernew.ui.listuser.ListUserFragment
import com.example.githubusernew.utils.SectionsPagerAdapter
import com.example.githubusernew.utils.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: DetailViewModel by viewModels {
        factory
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        val EXTRA_USERNAME = "extra_username"

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
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get data
        val dataId = arguments?.getInt(ListUserFragment.EXTRA_ID)
        val dataLogin = arguments?.getString(ListUserFragment.EXTRA_LOGIN)
        val dataAvatar = arguments?.getString(ListUserFragment.EXTRA_AVATAR)
        val dataHTML = arguments?.getString(ListUserFragment.EXTRA_HTML)

        // send data
        val mBundle = Bundle()
        mBundle.putString(EXTRA_USERNAME, dataLogin)

        // set detailLayout
        if (dataLogin != null) {
            setDetailUser(dataLogin)
        }

        // get detailLayout
        getDetailUser()
        // check user
        var checkId = viewModel.checkFavorite(id)

        if(checkId == null){
            binding.btnSave.setImageDrawable(ContextCompat.getDrawable(binding.btnSave.context, R.drawable.ic_not_saved))
        } else if (checkId != null){
            binding.btnSave.setImageDrawable(ContextCompat.getDrawable(binding.btnSave.context, R.drawable.ic_saved))
        }

        binding.btnSave.setOnClickListener{
            if(checkId == null){
                binding.btnSave.setImageDrawable(ContextCompat.getDrawable(binding.btnSave.context, R.drawable.ic_saved))
                // data disimpan kedalam database
                if (dataAvatar != null) {
                    if (dataLogin != null) {
                        if (dataId != null) {
                            if (dataHTML != null) {
                                viewModel.addFavorite(dataId,dataLogin,dataAvatar,dataHTML)
                            }
                        }
                    }
                }

            }else if(checkId != null){
                binding.btnSave.setImageDrawable(ContextCompat.getDrawable(binding.btnSave.context, R.drawable.ic_not_saved))
                // data dihapus dari database
                viewModel.deleteFavorite(id)
            }
        }

        // section pager adapter
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        (requireActivity() as AppCompatActivity).supportActionBar?.elevation = 0f
    }

    private fun getDetailUser() {
        viewModel.getDetailUser().observe(requireActivity()){result->
            if (result != null || result == null) {
                Glide.with(requireActivity())
                    .load(result.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfile)
                binding.tvUsername.text = result.login
                binding.tvName.text = result.name
                binding.tvUserCompany.text = result.company ?: "nothing"
                binding.tvFollower.text = result.followers.toString()
                binding.tvFollowing.text = result.following.toString()
                binding.tvRepository.text = result.reposUrl
                showLoading(false)
            }
        }
    }

    private fun setDetailUser(login: String) {
        viewModel.setDetaiUser(login)
        showLoading(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}