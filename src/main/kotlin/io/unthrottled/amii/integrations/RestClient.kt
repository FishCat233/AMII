package io.unthrottled.amii.integrations

import com.intellij.openapi.diagnostic.Logger
import io.unthrottled.amii.tools.readAllTheBytes
import io.unthrottled.amii.tools.toOptional
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.util.Optional
import java.util.concurrent.TimeUnit

object RestClient {
  private val httpClient = HttpClients.createMinimal()
  private val log = Logger.getInstance(this::class.java)

  private const val STATUS_OK = 200
  private const val ALLOWED_CONNECTION_TIMEOUT = 5L

  fun performGet(url: String): Optional<String> {
    log.info("Attempting to fetch remote asset: $url")
    val request = createGetRequest(url)
    return try {
      val response = httpClient.execute(request)
      val statusCode = response.statusLine.statusCode
      log.info("Asset has responded for remote asset: $url with status code $statusCode")
      if (statusCode == STATUS_OK) {
        response.entity.content.use { responseBody ->
          String(responseBody.readAllTheBytes())
        }.toOptional()
      } else {
        Optional.empty()
      }
    } catch (e: Throwable) {
      log.warn("Unable to get remote asset: $url for raisins", e)
      Optional.empty<String>()
    }
  }

  private fun createGetRequest(remoteUrl: String): HttpGet {
    val remoteAssetRequest = HttpGet(remoteUrl)
    remoteAssetRequest.addHeader("User-Agent", "Doki-Theme-Jetbrains")
    remoteAssetRequest.config = RequestConfig.custom()
      .setConnectTimeout(TimeUnit.MILLISECONDS.convert(ALLOWED_CONNECTION_TIMEOUT, TimeUnit.SECONDS).toInt())
      .build()
    return remoteAssetRequest
  }
}
