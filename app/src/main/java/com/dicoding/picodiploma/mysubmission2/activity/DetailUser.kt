package com.dicoding.picodiploma.mysubmission2.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mysubmission2.R
import com.dicoding.picodiploma.mysubmission2.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityDetailUserBinding
import com.dicoding.picodiploma.mysubmission2.utils.ShowLoading
import com.dicoding.picodiploma.mysubmission2.networking.DetailGitUser
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import com.dicoding.picodiploma.mysubmission2.viewModel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailUser : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var showLoading: ShowLoading
    private lateinit var progressBar: View
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private val detailViewModel by viewModels<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar
        showLoading = ShowLoading()

        sectionsPage()

        detailViewModel.detailUser.observe(this, {  detailUser ->
            setDetailUser(detailUser)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading.showLoading(it, progressBar)
        })

        getUser()

        binding.btShare.setOnClickListener(this)


    }

    private fun getUser() {
        val dataIntent = intent.getParcelableExtra<ItemsItem>(DETAIL_USER) as ItemsItem

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.userCheck(dataIntent.id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavUser.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavUser.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavUser.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                detailViewModel.addFavorite(dataIntent.login, dataIntent.id, dataIntent.avatarUrl)
            } else {
                detailViewModel.removeUser(dataIntent.id)
            }
            binding.toggleFavUser.isChecked = _isChecked
        }

        sectionsPagerAdapter.userName = dataIntent.login
        detailViewModel.getDetail(dataIntent.login)
        supportActionBar?.title = dataIntent.login
    }

    private fun setDetailUser(item: DetailGitUser) {
        binding.apply {
            tvNameUser.text = item.nameUser
            tvNama.text = item.name
            tvId.text = item.id.toString()
            tvCompany.text = item.company
            tvLocation.text = item.location
            tvRepository.text = item.repository.toString()
            tvFollower.text = item.followers.toString()
            tvFollowing.text = item.following.toString()
            Glide.with(this@DetailUser)
                .load(item.avatar)
                .into(imgAvatar)
        }
    }

    private fun sectionsPage() {
        sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onClick(v: View) {
        if (v == binding.btShare) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            val name = binding.tvNama.text
            share.putExtra(Intent.EXTRA_TEXT, name)
            startActivity(Intent.createChooser(share, getString(R.string.where)))
        }
    }

    companion object {
        const val DETAIL_USER = "detail_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


}