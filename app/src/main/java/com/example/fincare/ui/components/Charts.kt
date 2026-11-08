package com.example.fincare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fincare.ui.theme.FincareBlue
import java.util.Locale

fun formatBrl(amount: Double): String {
    return "R$ " + String.format(Locale("pt", "BR"), "%,.2f", amount)
}

val chartColors = listOf(
    Color(0xFF8B5CF6),
    Color(0xFFF0516E),
    Color(0xFF3B9BFF),
    Color(0xFF1FB57A),
    Color(0xFFFF9F1C),
)

@Composable
fun BarChart(labels: List<String>, values: List<Double>) {
    val max = values.maxOrNull() ?: 0.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        for (i in values.indices) {
            var fraction = 0.05f
            if (max > 0) {
                fraction = (values[i] / max).toFloat().coerceAtLeast(0.05f)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(fraction)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(FincareBlue),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = labels[i],
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
        }
    }
}

@Composable
fun CategoryBar(values: List<Double>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE6E8EC)),
    ) {
        for (i in values.indices) {
            if (values[i] > 0) {
                Box(
                    modifier = Modifier
                        .weight(values[i].toFloat())
                        .fillMaxHeight()
                        .background(chartColors[i % chartColors.size]),
                )
            }
        }
    }
}

@Composable
fun PercentRing(percent: Double) {
    Box(modifier = Modifier.size(46.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { (percent / 100).toFloat() },
            modifier = Modifier.size(46.dp),
            color = FincareBlue,
            trackColor = Color(0xFFD9DBE3),
            strokeWidth = 5.dp,
        )
        Text(
            text = "${percent.toInt()}%",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3A3F4B),
        )
    }
}
