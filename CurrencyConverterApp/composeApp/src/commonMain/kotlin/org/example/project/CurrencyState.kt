package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CurrencyState(val apiService: CurrencyApiService) {
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var exchangeRates by mutableStateOf<ExchangeRatesResponse?>(null)
    var amountToConvert by mutableStateOf("1.0")
    var selectedBaseCurrency by mutableStateOf("USD")
    var selectedTargetCurrency by mutableStateOf("EUR")
    var convertedAmount by mutableStateOf(0.0)
}