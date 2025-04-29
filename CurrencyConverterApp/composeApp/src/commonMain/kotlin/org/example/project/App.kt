package org.example.project

import androidx.compose.runtime.*

@Composable
fun App() {
    val apiKey = "7c871071fed6ef2379bb7a70"
    val apiService = remember { CurrencyApiService(apiKey) }
    val state = remember { CurrencyState(apiService) }

    var currentScreen by remember { mutableStateOf(Screen.Home) }

    LaunchedEffect(state.selectedBaseCurrency) {
        try {
            state.loading = true
            state.error = null
            state.exchangeRates = apiService.getExchangeRates(state.selectedBaseCurrency)
        } catch (e: Exception) {
            state.error = e.message
        } finally {
            state.loading = false
        }
    }

    LaunchedEffect(
        state.amountToConvert,
        state.selectedBaseCurrency,
        state.selectedTargetCurrency,
        state.exchangeRates
    ) {
        if (state.exchangeRates != null) {
            val amount = state.amountToConvert.toDoubleOrNull() ?: 0.0
            val rate = state.exchangeRates!!.conversion_rates[state.selectedTargetCurrency] ?: 0.0
            state.convertedAmount = amount * rate
        }
    }

    when (currentScreen) {
        Screen.Home -> HomeScreen(
            state = state,
            onNavigateToCalculator = { currentScreen = Screen.Calculator },
            onCurrencySelected = { currency ->
                state.selectedBaseCurrency = currency
            }
        )
        Screen.Calculator -> CalculatorScreen(
            state = state,
            onNavigateBack = { currentScreen = Screen.Home },
            onAmountChange = { state.amountToConvert = it },
            onBaseCurrencyChange = { state.selectedBaseCurrency = it },
            onTargetCurrencyChange = { state.selectedTargetCurrency = it }
        )
    }

    DisposableEffect(apiService) {
        onDispose {
            apiService.close()
        }
    }
}
