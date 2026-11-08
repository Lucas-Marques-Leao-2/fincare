package com.example.fincare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fincare.R
import com.example.fincare.domain.Contact
import com.example.fincare.ui.theme.Typography

@Composable
fun CardContact(contact: Contact) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    contact.name,
                    style = Typography.headlineLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Default.Call, contentDescription = "",
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    contact.number,
                    fontSize = 16.sp,
                    fontFamily = Font(R.font.inter).toFontFamily(),
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactCardPreview() {
    CardContact(
        Contact(
            id = 1,
            name = "Lucas",
            number = "82993409784"
        ),
    )
}