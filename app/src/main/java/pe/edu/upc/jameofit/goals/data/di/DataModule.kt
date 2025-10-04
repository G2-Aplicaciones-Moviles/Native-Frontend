package pe.edu.upc.jameofit.goals.data.di

import pe.edu.upc.jameofit.goals.data.remote.GoalsService
import pe.edu.upc.jameofit.goals.data.repository.GoalsRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit

object DataModule {
    fun getGoalsService(): GoalsService {
        return getRetrofit().create(GoalsService::class.java)
    }

    fun getGoalsRepository(): GoalsRepository {
        return GoalsRepository(getGoalsService())
    }
}