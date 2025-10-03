package pe.edu.upc.jameofit.shared.data.di

import androidx.room.Room
import okhttp3.OkHttpClient
import pe.edu.upc.jameofit.JameoFit
import pe.edu.upc.jameofit.shared.data.local.AppDatabase
import pe.edu.upc.jameofit.shared.data.remote.ApiConstants
import pe.edu.upc.jameofit.shared.data.remote.AuthInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SharedDataModule {
    private var dbInstance: AppDatabase? = null

    private var retrofitInstance: Retrofit? = null

    fun getRetrofit(): Retrofit {
        if (retrofitInstance == null) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitInstance!!
    }

    fun getAppDatabase(): AppDatabase {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(
                JameoFit.instance.applicationContext,
                AppDatabase::class.java,
                "jameofit_db"
            ).build()
        }
        return dbInstance!!
    }
}