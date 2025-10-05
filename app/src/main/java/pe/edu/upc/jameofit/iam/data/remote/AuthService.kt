package pe.edu.upc.jameofit.iam.data.remote

import pe.edu.upc.jameofit.iam.data.model.LoginRequest
import pe.edu.upc.jameofit.iam.data.model.LoginResponse
import pe.edu.upc.jameofit.iam.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("authentication/sign-in")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @POST("authentication/sign-up")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>
}