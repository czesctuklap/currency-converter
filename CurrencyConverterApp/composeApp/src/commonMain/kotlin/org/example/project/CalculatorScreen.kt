package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt


@Composable
fun CalculatorScreen(
    state: CurrencyState,
    onNavigateBack: () -> Unit,
    onAmountChange: (String) -> Unit,
    onBaseCurrencyChange: (String) -> Unit,
    onTargetCurrencyChange: (String) -> Unit
) {
    val currencyList = listOf("USD", "EUR", "PLN", "GBP", "JPY")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 50.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                text = "Calculator",
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(270.dp))

            BasicTextField(
                value = state.amountToConvert,
                onValueChange = onAmountChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.hsl(0f, 0f, 0.95f), RoundedCornerShape(25.dp))
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicText("From:", modifier = Modifier.padding(end = 8.dp))
                CurrencyDropdown(
                    options = currencyList,
                    selectedOption = state.selectedBaseCurrency,
                    onOptionSelected = onBaseCurrencyChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicText("To:", modifier = Modifier.padding(end = 8.dp))
                CurrencyDropdown(
                    options = currencyList,
                    selectedOption = state.selectedTargetCurrency,
                    onOptionSelected = onTargetCurrencyChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            BasicText(
                text = "Converted: ${(state.convertedAmount * 100).roundToInt() / 100.0} ${state.selectedTargetCurrency}",
                style = TextStyle(fontSize = 18.sp)
            )
        }

        // Back button
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 24.dp)
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)
                .height(50.dp)
                .background(
                    color = Color(0xFF262B39),
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable { onNavigateBack() },
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = "Back",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    }
}


@Composable
fun CurrencyDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Box(
            modifier = Modifier
                .clickable { expanded = true }
                .background(Color.hsl(0f, 0f, 0.95f), RoundedCornerShape(25.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BasicText(
                text = selectedOption,
                style = TextStyle(fontSize = 16.sp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                ) {
                    BasicText(
                        text = option,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}