package com.example.fincare.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fincare.ui.theme.FincareBlue

@Composable
fun FincareLogo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Canvas(modifier = Modifier.size(width = 56.dp, height = 44.dp)) {
            val roof = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height * 0.36f)
                lineTo(0f, size.height * 0.36f)
                close()
            }
            drawPath(roof, FincareBlue)

            val columnWidth = size.width * 0.12f
            val gap = size.width * 0.08f
            val top = size.height * 0.42f
            var x = size.width * 0.16f
            repeat(4) {
                drawRoundRect(
                    color = FincareBlue,
                    topLeft = Offset(x, top),
                    size = Size(columnWidth, size.height - top),
                    cornerRadius = CornerRadius(3f, 3f),
                )
                x += columnWidth + gap
            }
        }
        Text(
            text = "Fincare",
            color = FincareBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
    }
}
