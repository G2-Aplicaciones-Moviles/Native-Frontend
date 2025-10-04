package pe.edu.upc.jameofit.shared.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import pe.edu.upc.jameofit.shared.data.local.JwtStorage

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = JwtStorage.getToken()

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
