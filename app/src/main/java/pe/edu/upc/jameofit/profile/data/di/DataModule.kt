package pe.edu.upc.jameofit.profile.data.di

import pe.edu.upc.jameofit.profile.data.remote.ProfileService
import pe.edu.upc.jameofit.profile.data.repository.ProfileRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule

object DataModule {
    fun getProfileService(): ProfileService =
        SharedDataModule.getRetrofit().create(ProfileService::class.java)

    fun getProfileRepository(): ProfileRepository =
        ProfileRepository(getProfileService())
}
