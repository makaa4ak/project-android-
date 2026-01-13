package com.example.weather_project

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weather_project.databinding.ActivityMainBinding
import com.example.weather_project.model.CurrentWeatherResponse
import com.example.weather_project.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val API_KEY = "61661e133622d3aa7cb8f10fbd34c900"

    private val cities = listOf(
        "Киев",
        "Львов",
        "Одесса",
        "Днепр",
        "Харьков",
        "Запорожье",
        "Винница",
        "Чернигов",
        "Ивано-Франковск"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Автокомплит городов
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )
        binding.etCity.setAdapter(adapter)

        binding.btnSearch.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isNotEmpty()) {
                fetchWeatherByCity(city)
            } else {
                Toast.makeText(this, "Введите город", Toast.LENGTH_SHORT).show()
            }
        }

        // Город по умолчанию
        fetchWeatherByCity("Одесса")
    }

    private fun fetchWeatherByCity(city: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Получаем погоду по имени города
                val weather: CurrentWeatherResponse =
                    RetrofitClient.api.getWeatherByCity(
                        city = city,
                        apiKey = API_KEY,
                        units = "metric",
                        lang = "ru"


                    )


                // Обновляем UI на главном потоке
                withContext(Dispatchers.Main) {
                    updateUI(weather, city)
                }

            } catch (e: Exception) {
                Log.e("WeatherError", e.message ?: "Unknown error")
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка загрузки погоды",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateUI(weather: CurrentWeatherResponse, city: String) {
        val temp = weather.main.temp.toInt()
        val description = weather.weather[0].description.lowercase()
        val main = weather.weather[0].main.lowercase()

        binding.apply {
            tvCity.text = city
            tvTemp.text = "$temp°C"
            tvDescription.text = description.replaceFirstChar { it.uppercase() }
            ivWeather.setImageResource(getWeatherIcon(main, description))
            rootLayout.setBackgroundResource(
                getWeatherBackground(main, description)
            )
        }
    }

    private fun getWeatherIcon(main: String, description: String): Int {
        return when {
            "thunder" in main -> R.drawable.ic_thunder
            "snow" in main -> R.drawable.ic_snow
            "rain" in main -> R.drawable.ic_rain
            "cloud" in main && "few" in description -> R.drawable.ic_partly_cloudy
            "cloud" in main -> R.drawable.ic_cloudy
            "fog" in main || "mist" in main || "haze" in main -> R.drawable.ic_fog
            "clear" in main -> R.drawable.ic_sunny
            else -> R.drawable.ic_sunny
        }
    }

    private fun getWeatherBackground(main: String, description: String): Int {
        return when {
            "thunder" in main -> R.drawable.bg_rain
            "snow" in main -> R.drawable.bg_snow
            "rain" in main -> R.drawable.bg_rain
            "cloud" in main -> R.drawable.bg_cloudy
            "clear" in main -> R.drawable.bg_clear
            else -> R.drawable.bg_snow
        }
    }



}
