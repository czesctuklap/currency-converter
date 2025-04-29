package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: CurrencyState,
    onNavigateToCalculator: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    val currencyList = listOf("USD", "EUR", "PLN", "GBP", "JPY")
    var selectedCurrency by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 50.dp, bottom = 100.dp)
        ) {
            BasicText(
                text = "Currencies",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "Loading rates...",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    )
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "Error: ${state.error}",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    currencyList.forEach { currency ->
                        CurrencyItem(currency) {
                            selectedCurrency = currency
                            onCurrencySelected(currency)
                            coroutineScope.launch {
                                try {
                                    state.loading = true
                                    state.error = null
                                    state.exchangeRates = state.apiService.getExchangeRates(currency)
                                } catch (e: Exception) {
                                    state.error = e.message ?: "Unknown error"
                                } finally {
                                    state.loading = false
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)
                .height(50.dp)
                .background(
                    color = Color(0xFF262B39),
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable {
                    onNavigateToCalculator()
                },
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "Calculate",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }

        if (selectedCurrency != null && state.exchangeRates != null) {
            Dialog(onDismissRequest = { selectedCurrency = null }) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(25.dp))
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BasicText(
                            text = "$selectedCurrency Exchange Rates",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        val rates = state.exchangeRates!!.conversion_rates
                        val sampleCurrencies = currencyList.filter { it != selectedCurrency }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            sampleCurrencies.take(3).forEach { currency ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    BasicText(
                                        text = "1 $selectedCurrency =",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.DarkGray
                                        )
                                    )
                                    BasicText(
                                        text = "${rates[currency] ?: 0.0} $currency",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color(0xFF262B39),
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        BasicText(
                            text = "Last updated: ${state.exchangeRates!!.time_last_update_utc}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Box(
                            modifier = Modifier
                                .background(Color(0xFF262B39), RoundedCornerShape(25.dp))
                                .clickable { selectedCurrency = null }
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            BasicText(
                                text = "Close",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyItem(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        BasicText(
            text = name,
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.DarkGray
            )
        )
    }
}