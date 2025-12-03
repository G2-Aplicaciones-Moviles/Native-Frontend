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

    suspend fun sendContact(
        patientUserId: Long,
        nutritionistId: Long,
        serviceType: String,
        startDate: String? = null,
        scheduledAt: String? = null
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val body = CreateNutritionistPatientRequest(
                    patientUserId = patientUserId,
                    nutritionistId = nutritionistId,
                    serviceType = serviceType,
                    startDate = startDate,
                    scheduledAt = scheduledAt
                )
                val response = api.sendContactRequest(body)
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    suspend fun getNutritionistsOfPatient(patientId: Long): List<NutritionistResponse> =
        withContext(Dispatchers.IO) {
            val res = api.getNutritionistsOfPatient(patientId)
            res.body() ?: emptyList()
        }
}