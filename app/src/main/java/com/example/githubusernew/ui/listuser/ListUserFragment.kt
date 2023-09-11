package com.example.githubusernew.ui.listuser

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernew.R
import com.example.githubusernew.data.remote.model.ItemsItem
import com.example.githubusernew.databinding.FragmentListUserBinding
import com.example.githubusernew.ui.adapter.SearchUserAdapter
import com.example.githubusernew.utils.UserViewModelFactory

class ListUserFragment : Fragment() {

    private val factory: UserViewModelFactory by lazy {
        UserViewModelFactory.getInstance(requireActivity())
    }
    private val viewModel: ListUserViewModel by viewModels {
        factory
    }

    private var _binding: FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<ItemsItem>()
    private val listAdapter = SearchUserAdapter(list)

    companion object {
        val EXTRA_ID = "extra_id"
        val EXTRA_LOGIN = "extra_login"
        val EXTRA_AVATAR = "extra_avatar"
        val EXTRA_HTML = "extra_html"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // search user
        binding.myEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                findUser()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // fab
        binding.fabFavorite.setOnClickListener{
            view.findNavController().navigate(R.id.action_listUserFragment_to_favoriteUserFragment)
        }

        listAdapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {

                val mBundle = Bundle()
                mBundle.putInt(EXTRA_ID, data.id)
                mBundle.putString(EXTRA_LOGIN,data.login)
                mBundle.putString(EXTRA_AVATAR,data.avatarUrl)
                mBundle.putString(EXTRA_HTML,data.htmlUrl)
                view.findNavController().navigate(R.id.action_listUserFragment_to_detailFragment, mBundle)
            }
        })

        // adapter
        showRecycler()

        // get user
        getListUser()
    }

    fun showRecycler(){
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = listAdapter
    }

    private fun getListUser() {
        viewModel.getSearchUser().observe(requireActivity()) { result ->
            if (result != null){
                showLoading(false)
                listAdapter.setListUser(result)
            }
        }
    }

    private fun findUser() {
        binding.apply {
            val query = myEditText.text.toString()

            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu -> {
                // Handle option 2 click
                findNavController().navigate(R.id.action_listUserFragment_to_darkSettingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbListUser.visibility = View.VISIBLE
        } else {
            binding.pbListUser.visibility = View.GONE
        }
    }
}