package com.dicoding.picodiploma.mysubmission2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.adapter.FollowAdapter
import com.dicoding.picodiploma.mysubmission2.databinding.FragmentFollowBinding
import com.dicoding.picodiploma.mysubmission2.utils.ShowLoading
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import com.dicoding.picodiploma.mysubmission2.viewModel.FollowViewModel

class FragmentFollow: Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var name: String
    private lateinit var showLoading: ShowLoading
    private lateinit var progressBar: View
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading = ShowLoading()
        progressBar = binding.progressBar

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        name = arguments?.getString(USERNAME)!!

        followViewModel.follower.observe(viewLifecycleOwner, { follower ->
            setDataFollow(follower)
        })

        followViewModel.following.observe(viewLifecycleOwner, { following ->
            setDataFollow(following)
        })

        followViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading.showLoading(it, progressBar)
        })

        when (index) {
            1 -> followViewModel.getFollow(name)
            2 -> followViewModel.getFollowing(name)
        }
    }

    private fun setDataFollow(items: ArrayList<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (i in items) {
            val user = ItemsItem(i.avatarUrl, i.id, i.login)
            listUser.addAll(listOf(user))
        }
        val adapter = FollowAdapter(listUser)
        binding.apply {
            listfollow.layoutManager = LinearLayoutManager(context)
            listfollow.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }
}