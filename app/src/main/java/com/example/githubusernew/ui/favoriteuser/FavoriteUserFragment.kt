package com.example.githubusernew.ui.favoriteuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.data.local.FavoriteEntity
import com.example.githubusernew.databinding.FragmentFavoriteUserBinding
import com.example.githubusernew.ui.adapter.FavoriteAdapter
import com.example.githubusernew.ui.listuser.ListUserFragment
import com.example.githubusernew.utils.UserViewModelFactory
import java.util.ArrayList

class FavoriteUserFragment : Fragment() {

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: FavoriteUserViewModel by viewModels {
        factory
    }

    private var _binding: FragmentFavoriteUserBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<FavoriteEntity>()
    private val favoriteAdapter = FavoriteAdapter(list)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteEntity) {
                val mBundle = Bundle()
                mBundle.putInt(ListUserFragment.EXTRA_ID, data.id)
                mBundle.putString(ListUserFragment.EXTRA_LOGIN,data.login)
                mBundle.putString(ListUserFragment.EXTRA_AVATAR,data.avatar_url)
                mBundle.putString(ListUserFragment.EXTRA_HTML,data.html_url)
                view.findNavController().navigate(R.id.action_listUserFragment_to_detailFragment)
            }
        })

        // adapter setting
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = favoriteAdapter

        // get favorite
        getFavorite()
    }

    private fun getFavorite(){
        viewModel.getFavoriteUser()?.observe(requireActivity()){result->
            if (result != null){
                favoriteAdapter.setListUser(result)
            }
        }
    }

}