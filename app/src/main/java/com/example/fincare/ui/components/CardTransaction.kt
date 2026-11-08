package com.example.fincare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fincare.domain.Transaction
import com.example.fincare.sample.sampleDataTransaction
import com.example.fincare.ui.theme.FincareEarning
import com.example.fincare.ui.theme.FincareExpense
import java.util.Locale

@Composable
fun CardTransaction(transaction: Transaction) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(categoryColor(transaction.category)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = transaction.category.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = transaction.description,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = "${transaction.category} · ${transaction.bankName}",
                        color = Color(0xFF6C6C6C),
                        fontSize = 13.sp,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatMoney(transaction.amount, transaction.type),
                        color = if (transaction.type == "Receita") FincareEarning else FincareExpense,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    )
                    Text(
                        text = transaction.date,
                        color = Color(0xFF6C6C6C),
                        fontSize = 12.sp,
                    )
                }
            }
            HorizontalDivider(color = Color(0xFFF0F0F0))
        }
    }
}

private fun categoryColor(category: String): Color {
    return when (category.lowercase()) {
        "casa", "home" -> Color(0xFF42A5F5)
        "apps" -> Color(0xFFEC407A)
        "viagem", "travel" -> Color(0xFF26C6DA)
        "livros", "books" -> Color(0xFFFFA726)
        "educação", "educacao", "education" -> Color(0xFF66BB6A)
        "investimentos", "investments" -> Color(0xFFAB47BC)
        "lazer", "entertainment" -> Color(0xFFEF5350)
        "alimentação", "alimentacao", "food" -> Color(0xFFFF7043)
        else -> Color(0xFF29B6F6)
    }
}

private fun formatMoney(amount: Double, type: String): String {
    val formatted = String.format(Locale("pt", "BR"), "%.2f", amount)
    val sign = if (type == "Receita") "+" else "-"
    return "$sign R$ $formatted"
}

@Preview(showBackground = true)
@Composable
fun CardTransactionPreview() {
    CardTransaction(sampleDataTransaction)
}
