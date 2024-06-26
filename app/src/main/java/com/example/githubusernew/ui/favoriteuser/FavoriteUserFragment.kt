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

    companion object {
        val EXTRA_LOGIN = "extra_login"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: FavoriteEntity) {
                val mBundle = Bundle()
                mBundle.putString(EXTRA_LOGIN,data.login)
                mBundle.putParcelable(ListUserFragment.EXTRA_PARCEL,data)
                view.findNavController().navigate(R.id.action_favoriteUserFragment_to_detailFragmentFavorite,mBundle)
            }
        })

        // adapter setting
        showRecycler()

        // get favorite
        getFavorite()
    }

    private fun showRecycler() {
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = favoriteAdapter
    }

    private fun getFavorite(){
        viewModel.getFavoriteUser()?.observe(viewLifecycleOwner){result->
            favoriteAdapter.setListUser(result)
        }
    }

}