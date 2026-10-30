package com.example.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

// Компонент теперь переиспользуемый!
@Composable
fun DateCalendar(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val dateList = remember {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        for (i in 0 until 6) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        dates
    }

    val dateFormat = remember { SimpleDateFormat("dd.MM", Locale.getDefault()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(dateList) { date ->
            val dateString = dateFormat.format(date)
            val isSelected = dateString == selectedDate

            Card(
                modifier = Modifier
                    .width(60.dp)
                    .height(70.dp)
                    .clickable { onDateSelected(dateString) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = dateString,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}