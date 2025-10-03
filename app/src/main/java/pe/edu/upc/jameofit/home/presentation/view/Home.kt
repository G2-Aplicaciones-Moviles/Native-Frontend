package pe.edu.upc.jameofit.home.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Home(recordarPantalla: NavHostController){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp)
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOME STUDENT",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Blue
        )
    }
}