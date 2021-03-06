package com.piotrjanczyk.qrcodegenerator.web

import com.piotrjanczyk.qrcodegenerator.proto.QrCodeServiceGrpcKt.QrCodeServiceCoroutineStub
import com.piotrjanczyk.qrcodegenerator.web.ErrorCorrection.*
import com.piotrjanczyk.qrcodegenerator.web.ImageFormat.PNG
import com.piotrjanczyk.qrcodegenerator.web.ImageFormat.SVG
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.springframework.stereotype.Component
import java.io.Closeable
import java.util.concurrent.TimeUnit
import com.piotrjanczyk.qrcodegenerator.proto.CreateQrCodeRequest as PbCreateQrCodeRequest
import com.piotrjanczyk.qrcodegenerator.proto.ErrorCorrection as PbErrorCorrection
import com.piotrjanczyk.qrcodegenerator.proto.ImageFormat as PbImageFormat

@Component
class QrCodeServiceClient(
  config: Config
) : Closeable {
  private val channel = ManagedChannelBuilder
    .forTarget(config.qrCodeServiceAddress)
    .usePlaintext()
    .executor(Dispatchers.Default.asExecutor())
    .build()

  private val stub = QrCodeServiceCoroutineStub(channel)

  suspend fun createQrCode(definition: QrCodeDefinition): ByteArray {
    val request = PbCreateQrCodeRequest.newBuilder()
      .setData(definition.data)
      .setVersion(definition.version ?: 0)
      .setErrorCorrection(when (definition.errorCorrection) {
        LOW -> PbErrorCorrection.LOW
        MEDIUM -> PbErrorCorrection.MEDIUM
        QUARTILE -> PbErrorCorrection.QUARTILE
        HIGH -> PbErrorCorrection.HIGH
      })
      .setFillColor(definition.foregroundColor)
      .setBackColor(definition.backgroundColor)
      .setBoxSize(definition.boxSizeOrDefault)
      .setBorder(definition.borderOrDefault)
      .setImageFormat(when (definition.imageFormat) {
        PNG -> PbImageFormat.PNG
        SVG -> PbImageFormat.SVG
      })
      .build()
    val response = stub.createQrCode(request)
    return response.file.toByteArray()
  }

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}
