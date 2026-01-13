package com.example.weather_project.network

import com.example.weather_project.model.GeoResponse
import com.example.weather_project.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Геокодинг (город → координаты)
    @GET("geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("limit") limit: Int = 1
    ): List<GeoResponse>

    // Погода по координатам
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): CurrentWeatherResponse


}
