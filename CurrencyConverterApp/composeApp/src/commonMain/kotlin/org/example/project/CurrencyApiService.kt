package org.example.project

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.*

class CurrencyApiService(private val apiKey: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getExchangeRates(baseCurrency: String): ExchangeRatesResponse {
        val url = "https://v6.exchangerate-api.com/v6/$apiKey/latest/$baseCurrency"
        return client.get(url).body()
    }

    fun close() {
        client.close()
    }
}