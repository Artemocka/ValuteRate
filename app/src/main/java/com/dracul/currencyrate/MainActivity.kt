package com.dracul.currencyrate

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.dracul.currencyrate.api.ValutApi
import com.dracul.currencyrate.data.DailyData
import com.dracul.currencyrate.databinding.ActivityMainBinding
import com.example.technotestvk.recycler.ValuteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val valutApi = getRetrofitClient().create(ValutApi::class.java)
    private val adapter = ValuteAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        lateinit var dailyData:  DailyData

        lifecycleScope.launch(Dispatchers.IO) {
            dailyData = valutApi.getData()
            lifecycleScope.launch(Dispatchers.Main){
                adapter.submitList(dailyData.getListOfValutes())
//                binding.text.text = adapter.currentList.toString()
                Log.e("",adapter.currentList.toString())
            }
        }
        binding.list.adapter = adapter


        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContentView(binding.root)

    }

    private fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.cbr-xml-daily.ru").addConverterFactory(GsonConverterFactory.create()).build()
    }
}