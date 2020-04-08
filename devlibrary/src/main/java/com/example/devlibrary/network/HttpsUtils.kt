package com.example.devlibrary.network

import com.example.devlibrary.BuildConfig
import com.example.devlibrary.app.App
import com.example.devlibrary.network.cookies.NovateCookieManger
import com.example.devlibrary.network.interceptor.HeaderInterceptor
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Description: 网络请求，主要用于获取Retrofit
 * Created by FQH on 2019/10/14.
 */
object HttpsUtils {

    private const val DEFAULT_TIMEOUT: Long = 10L
    private val client: OkHttpClient by lazy { getOkHttpClient() }

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE

        val httpCacheDir = File(App.sContext.cacheDir, "api_cache")
        val cache = Cache(httpCacheDir, (10 * 1024 * 1024).toLong())

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        val client = OkHttpClient.Builder()
            //信任所有证书，不安全！！！谨记
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(hostnameVerifier)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(HeaderInterceptor())
            .cookieJar(NovateCookieManger(App.sContext))
            .cache(cache)
                //下面缓存拦截器会拦截网络请求，可能导致请求参数一致请求的只走缓存，不走服务器
//            .addNetworkInterceptor(CacheInterceptor(App.sContext))
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
            .build()
        return client
    }

    fun getRetrofit(apiUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            //效验客户端证书
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            //效验服务端证书
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })

    private val hostnameVerifier: HostnameVerifier = object : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            //效验服务器证书域名是否相符,
            val hv = HttpsURLConnection.getDefaultHostnameVerifier()
            return hv.verify(hostname, session)
        }
    }

}