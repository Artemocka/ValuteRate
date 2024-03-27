package com.dracul.currencyrate

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.lifecycleScope
import com.dracul.currencyrate.api.ValutApi
import com.dracul.currencyrate.data.DailyData
import com.dracul.currencyrate.databinding.ActivityMainBinding
import com.example.technotestvk.recycler.ValuteAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val valutApi = getRetrofitClient().create(ValutApi::class.java)
    private val adapter = ValuteAdapter()
    private val internetProblems = MutableStateFlow(false)
    private val isProgress = MutableStateFlow(false)
    private var date: LocalDateTime? = null
    private var dailyData: DailyData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        loadData()
        binding.list.adapter = adapter


        lifecycleScope.launch {
            internetProblems.collect {
                binding.btnRefresh.isVisible = it
                binding.ivNoInternet.isVisible = it
                binding.tvNoInternet.isVisible = it
            }

        }
        lifecycleScope.launch {
            isProgress.collect {
                binding.pbProgress.isVisible = it
                binding.list.isVisible = !it
            }
        }

        binding.btnRefresh.setOnClickListener {
            loadData()
        }

        binding.toolbar.menu.findItem(R.id.refresh_item).setOnMenuItemClickListener {
            loadData()
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.toolbar.updatePaddingRelative(top = systemBars.top)
            binding.list.updatePaddingRelative(
                bottom = binding.list.paddingTop + insets.getInsets(
                    WindowInsetsCompat.Type.navigationBars()
                ).bottom
            )
            insets
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)

        val handler = android.os.Handler(Looper.getMainLooper())
        val runnableCode: Runnable = object : Runnable {
            override fun run() {
                loadData()
                handler.postDelayed(this, 30000)
            }
        }
        handler.post(runnableCode)
    }

    private fun setDateToToolbar() {
        val updated = getString(R.string.updated)
        binding.toolbar.title = "$updated: ${date?.format(DateTimeFormatter.ISO_LOCAL_DATE)} ${date?.format(DateTimeFormatter.ISO_TIME)}"
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                isProgress.value = true
                internetProblems.value = false
                dailyData = valutApi.getData()
                date = dailyData?.getDate()
                if (internetProblems.value) internetProblems.value = false

            } catch (_: Exception) {
                isProgress.value = false
                internetProblems.value = true
                withContext(Dispatchers.Main) {
                    binding.list.isVisible = false
                    Snackbar.make(binding.root, "Oops, something went wrong!", Snackbar.LENGTH_LONG).show()
                }
            }

            withContext(Dispatchers.Main) {
                isProgress.value = false
                date?.run {
                    setDateToToolbar()
                }
                dailyData?.run {
                    adapter.submitList(getListOfValutes())
                }
                Log.e("", adapter.currentList.toString())
            }
        }
    }

    private fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.cbr-xml-daily.ru").addConverterFactory(GsonConverterFactory.create()).build()
    }
}