package pe.edu.upc.jameofit.iam.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.R


@Composable
fun Welcome(navController: NavHostController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 80.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "Icono de la aplicación",
                modifier = Modifier
                    .size(280.dp)
                    .padding(bottom = 15.dp)
            )

            Text(text = "Bienvenido",
                fontWeight = FontWeight.SemiBold,
                fontSize = 26.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Text(
                text = "Tu compañero inteligente para alcanzar tus metas de salud y bienestar.",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            Text(text = "¡Empieza hoy tu transformación!",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(5.dp)
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .height(48.dp),
                onClick = {
                        navController.navigate("V3")
                }
            ) {
                Text(
                    text = "Registrarse",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Text(
                text = "O",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .height(48.dp),
                onClick = {
                    navController.navigate("V2")
                }
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }

}