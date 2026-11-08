package com.example.fincare.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fincare.R
import com.example.fincare.ui.theme.FincareBlue

class BottomBarItem(
    val route: String,
    val label: String,
    val icon: Int,
)

val bottomBarItems = listOf(
    BottomBarItem("transactionExploreScreen", "extrato", R.drawable.ic_nav_extract),
    BottomBarItem("accountsScreen", "contas", R.drawable.ic_nav_accounts),
    BottomBarItem("homeScreen", "início", R.drawable.ic_nav_home),
    BottomBarItem("investmentsScreen", "investimentos", R.drawable.ic_nav_investments),
    BottomBarItem("profileScreen", "perfil", R.drawable.ic_nav_profile),
)

@Composable
fun FincareBottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FincareBlue)
            .navigationBarsPadding()
            .height(72.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        for (item in bottomBarItems) {
            val selected = item.route == currentRoute

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavigate(item.route) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-12).dp)
                            .size(50.dp)
                            .background(Color.White, CircleShape)
                            .border(4.dp, FincareBlue, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.label,
                            tint = FincareBlue,
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.label,
                        tint = Color.White,
                    )
                }

                Text(
                    text = item.label,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.height(18.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun FincareBottomBarPreview() {
    FincareBottomBar(currentRoute = "homeScreen", onNavigate = {})
}
