package com.example.weather_project.model

data class OneCallResponse(
    val current: Current,
    val hourly: List<Hourly>
)

data class Current(
    val temp: Double,
    val weather: List<WeatherInfo>
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherInfo>
)

data class WeatherInfo(
    val description: String,
    val icon: String
)
