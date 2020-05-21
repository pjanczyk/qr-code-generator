package com.piotrjanczyk.qrcodegenerator.web

import com.piotrjanczyk.qrcodegenerator.proto.CreateQrCodeRequest
import com.piotrjanczyk.qrcodegenerator.proto.QrCodeServiceGrpcKt.QrCodeServiceCoroutineStub
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.springframework.stereotype.Component
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Component
class QrCodeServiceClient(
  private val config: Config
) : Closeable {
  private val channel = ManagedChannelBuilder
    .forAddress(config.qrCodeService.host, config.qrCodeService.port)
    .usePlaintext()
    .executor(Dispatchers.Default.asExecutor())
    .build()

  private val stub = QrCodeServiceCoroutineStub(channel)

  suspend fun createQrCode(
    data: String
  ): ByteArray {
    val request = CreateQrCodeRequest.newBuilder()
      .setData(data)
      .build()
    val response = stub.createQrCode(request)
    return response.file.toByteArray()
  }

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}
