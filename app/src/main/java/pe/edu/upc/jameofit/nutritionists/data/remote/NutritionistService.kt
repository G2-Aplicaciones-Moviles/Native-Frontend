package pe.edu.upc.jameofit.nutritionists.data.remote

import pe.edu.upc.jameofit.nutritionists.data.model.CreateNutritionistPatientRequest
import pe.edu.upc.jameofit.nutritionists.data.model.NutritionistResponse
import retrofit2.Response
import retrofit2.http.*

interface NutritionistService {

    @GET("/api/v1/nutritionists")
    suspend fun getAllNutritionists(): Response<List<NutritionistResponse>>

    @GET("/api/v1/nutritionist-patients/patient/{patientId}")
    suspend fun getNutritionistsOfPatient(
        @Path("patientId") patientId: Long
    ): Response<List<NutritionistResponse>>

    @POST("/api/v1/nutritionist-patients")
    suspend fun sendContactRequest(
        @Body request: CreateNutritionistPatientRequest
    ): Response<Unit>
}
