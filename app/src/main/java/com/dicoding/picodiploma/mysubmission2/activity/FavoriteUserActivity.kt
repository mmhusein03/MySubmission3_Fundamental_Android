package com.dicoding.picodiploma.mysubmission2.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.R
import com.dicoding.picodiploma.mysubmission2.adapter.UserAdapter
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityFavoriteUserBinding
import com.dicoding.picodiploma.mysubmission2.local.FavoriteUser
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import com.dicoding.picodiploma.mysubmission2.viewModel.FavUserViewModel

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private val favUserViewModel by viewModels<FavUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favUserViewModel.getFavUser()?.observe(this, {
            showUser(it)
        })
        showRecycleView()

        supportActionBar?.title = getString(R.string.title_favorite)
    }

    private fun showUser(items: List<FavoriteUser>) {
        val listUser = ArrayList<ItemsItem>()
        val layoutEmpty = findViewById<View>(R.id.empty_layout)
        for (i in items) {
            val user = ItemsItem(i.avatar, i.id, i.name)
            listUser.add(user)
        }

        if (listUser.size == 0) {
            layoutEmpty.visibility = View.VISIBLE
            binding.emptyLayout.tvMessage.text = getString(R.string.message_empty_favorite)
        } else {
            layoutEmpty.visibility = View.GONE
        }

        val userAdapter = UserAdapter(listUser)

        binding.listfavorite.apply {
            setHasFixedSize(true)
            adapter = userAdapter
        }

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {

                val moveDetailUser = Intent(this@FavoriteUserActivity, DetailUser::class.java)
                moveDetailUser.putExtra(DetailUser.DETAIL_USER, data)
                startActivity(moveDetailUser)
            }
        })

    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.listfavorite.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.listfavorite.layoutManager = layoutManager
        }
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listfavorite.addItemDecoration(itemDecoration)
    }
}