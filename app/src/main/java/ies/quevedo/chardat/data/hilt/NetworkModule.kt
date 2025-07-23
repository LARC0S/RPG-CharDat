package ies.quevedo.chardat.data.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ies.quevedo.chardat.data.utils.Constants
import ies.quevedo.chardat.network.*
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Duration
import java.time.temporal.ChronoUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val baseUrl = Constants.BASE_URL

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .protocols((listOf(Protocol.HTTP_1_1)))
            .readTimeout(Duration.of(10, ChronoUnit.MINUTES))
            .callTimeout(Duration.of(10, ChronoUnit.MINUTES))
            .connectTimeout(Duration.of(10, ChronoUnit.MINUTES))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providesArmaduraService(retrofit: Retrofit): ArmaduraService {
        return retrofit.create(ArmaduraService::class.java)
    }

    @Provides
    fun provideArmaService(retrofit: Retrofit): ArmaService {
        return retrofit.create(ArmaService::class.java)
    }

    @Provides
    fun providesEscudoService(retrofit: Retrofit): EscudoService {
        return retrofit.create(EscudoService::class.java)
    }

    @Provides
    fun providesObjetoService(retrofit: Retrofit): ObjetoService {
        return retrofit.create(ObjetoService::class.java)
    }

    @Provides
    fun providePersonajeService(retrofit: Retrofit): PersonajeService {
        return retrofit.create(PersonajeService::class.java)
    }
}