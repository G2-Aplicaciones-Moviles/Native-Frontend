package pe.edu.upc.jameofit.nutritionists.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.nutritionists.data.model.CreateNutritionistPatientRequest
import pe.edu.upc.jameofit.nutritionists.data.model.NutritionistResponse
import pe.edu.upc.jameofit.nutritionists.data.remote.NutritionistService

class NutritionistRepository(
    private val api: NutritionistService
) {

    suspend fun getNutritionists(): List<NutritionistResponse> =
        withContext(Dispatchers.IO) {
            val res = api.getAllNutritionists()
            res.body() ?: emptyList()
        }

    suspend fun sendContact(patientId: Long, nutritionistId: Long): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val body = CreateNutritionistPatientRequest(
                    patientId = patientId,
                    nutritionistId = nutritionistId
                )
                val response = api.sendContactRequest(body)
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
}
