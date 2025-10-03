package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.content.SharedPreferences
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
import pe.edu.upc.jameofit.MainActivity
import pe.edu.upc.jameofit.R


@Composable
fun SetupDone(recordarPantalla: NavHostController,  mainActivity: MainActivity){

    val prefProfile: SharedPreferences = mainActivity.getSharedPreferences("pref_profile", Context.MODE_PRIVATE)


    val registeredName: String = prefProfile.getString("nombre", "")!!
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
            Text(text = "¡Bienvenido $registeredName!",
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 7.dp)
            )

            Text(text = "Gracias por completar su información",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Icono de la aplicación",
                modifier = Modifier
                    .size(280.dp)
            )

            Text(text = "Ahora puede acceder a la aplicación y descubrir lo que JameoFit tiene para ti.",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(15.dp)
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                onClick = {
                    recordarPantalla.navigate("V7")
                }
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }

}