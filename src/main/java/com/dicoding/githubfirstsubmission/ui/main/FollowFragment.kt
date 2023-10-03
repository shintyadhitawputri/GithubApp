package com.dicoding.githubfirstsubmission.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubfirstsubmission.data.response.ItemsItem
import com.dicoding.githubfirstsubmission.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private var username: String? = null
    private var position: Int = 0
    private lateinit var binding: FragmentFollowBinding
    companion object {
        const val ARG_USERNAME = "section_username"
        const val ARG_POSITION = "section_position"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        showLoading(true)
        username?.let { mainViewModel.fetchFollow(it) }
        if (position == 1) {
                mainViewModel.listFollowers.observe(viewLifecycleOwner) { listFollower ->
                    setFollowersData(listFollower)
                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.rvFollower.layoutManager = layoutManager
                    val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
                    binding.rvFollower.addItemDecoration(itemDecoration)
                    showLoading(false)
                }
        } else {
                mainViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                    setFollowingData(listFollowing)
                    val layoutManager = LinearLayoutManager(requireContext())
                    binding.rvFollowing.layoutManager = layoutManager
                    val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
                    binding.rvFollowing.addItemDecoration(itemDecoration)
                    showLoading(false)
                }
        }
    }


    private fun setFollowersData(listFollowers: List<ItemsItem>) {
        val adapter = UsernameAdapter(listFollowers)
        binding.rvFollower.adapter = adapter
        binding.rvFollower.visibility = View.VISIBLE
        binding.rvFollowing.visibility = View.GONE

        adapter.updateData(listFollowers)
    }

    private fun setFollowingData(listFollowing: List<ItemsItem>) {
        val adapter = UsernameAdapter(listFollowing)
        binding.rvFollowing.adapter = adapter
        binding.rvFollowing.visibility = View.VISIBLE
        binding.rvFollower.visibility = View.GONE

        adapter.updateData(listFollowing)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}