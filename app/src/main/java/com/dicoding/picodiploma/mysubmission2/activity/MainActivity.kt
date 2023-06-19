package com.dicoding.picodiploma.mysubmission2.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.mysubmission2.R
import com.dicoding.picodiploma.mysubmission2.adapter.UserAdapter
import com.dicoding.picodiploma.mysubmission2.databinding.ActivityMainBinding
import com.dicoding.picodiploma.mysubmission2.utils.ShowLoading
import com.dicoding.picodiploma.mysubmission2.networking.ItemsItem
import com.dicoding.picodiploma.mysubmission2.utils.SettingPreferences
import com.dicoding.picodiploma.mysubmission2.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var showLoading: ShowLoading
    private lateinit var progressBar: View
    private val mainViewModel by viewModels<MainViewModel>{
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title)

        showLoading = ShowLoading()
        progressBar = binding.progressBar

        findUser()
        mainViewModel.user.observe(this, { user ->
            setUserData(user)
        })

        showRecycleView()

        mainViewModel.userCount.observe(this, { userCount ->
            setUserCount(userCount)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading.showLoading(it, progressBar)
        })

        darkMode()
    }

    private fun findUser() {
        binding.searchView.apply {
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.findUser(query)
                    hideKeyboard(progressBar)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
    }

    private fun setUserCount(user: Int){
        if (user == 0) {
            binding.count.text = user.toString()
            Toast.makeText(this@MainActivity, getString(R.string.usertNotFound), Toast.LENGTH_SHORT).show()
        } else {
            binding.count.text = user.toString()
        }
    }

    private fun setUserData(items: List<ItemsItem>) {
        val layoutEmpty = findViewById<View>(R.id.empty_layout)
        val listUser = ArrayList<ItemsItem>()
        for (i in items) {
            val user = ItemsItem(i.avatarUrl, i.id, i.login)
            listUser.addAll(listOf(user))
        }

        if (listUser.size == 0) {
            layoutEmpty.visibility = View.VISIBLE
            binding.emptyLayout.tvMessage.text = getString(R.string.message_empty_search)
        } else {
            layoutEmpty.visibility = View.GONE
        }

        val adapter = UserAdapter(listUser)
        binding.liUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {

                val moveDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                moveDetailUser.putExtra(DetailUser.DETAIL_USER, data)
                startActivity(moveDetailUser)
            }
        })
    }

    private fun showRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.liUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.liUser.layoutManager = layoutManager
        }
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.liUser.addItemDecoration(itemDecoration)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuFavorite -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun darkMode() {
        mainViewModel.getTheme().observe(this) { isChecked ->
            if (isChecked) {
                binding.switchTheme.text = getString(R.string.darkMode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                binding.switchTheme.text = getString(R.string.lightMode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            changeIcon(isChecked)
            binding.switchTheme.isChecked = isChecked
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveTheme(isChecked)
        }
    }

    private fun changeIcon(state: Boolean) {
        if (state) {
            binding.iconMode.setImageResource(R.drawable.ic_baseline_nights)
        } else {
            binding.iconMode.setImageResource(R.drawable.ic_baseline_wb_sunny)
        }
    }
}