package com.piotrjanczyk.qrcodegenerator.web

import com.piotrjanczyk.qrcodegenerator.web.ImageFormat.PNG
import com.piotrjanczyk.qrcodegenerator.web.ImageFormat.SVG
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ImageController(
  private val qrCodeServiceClient: QrCodeServiceClient
) {
  @GetMapping("/qr")
  suspend fun getImage(
    @RequestParam("data") data: String,
    @RequestParam("format", required = false) imageFormat: ImageFormat?,
    @RequestParam("ec", required = false) errorCorrection: ErrorCorrection?,
    @RequestParam("version", required = false) version: Int?,
    @RequestParam("fg", required = false) foregroundColor: String?,
    @RequestParam("bg", required = false) backgroundColor: String?,
    @RequestParam("box", required = false) boxSize: Int?,
    @RequestParam("border", required = false) border: Int?
  ): ResponseEntity<ByteArray> {
    val definition = QrCodeDefinition.build(
      data = data,
      imageFormat = imageFormat,
      version = version,
      errorCorrection = errorCorrection,
      foregroundColor = foregroundColor,
      backgroundColor = backgroundColor,
      boxSize = boxSize,
      border = border
    )
    val bytes = qrCodeServiceClient.createQrCode(definition)
    val headers = HttpHeaders().apply {
      contentType = when (definition.imageFormat) {
        PNG -> MediaType.IMAGE_PNG
        SVG -> MediaType("image/svg+xml")
      }
    }
    return ResponseEntity(bytes, headers, HttpStatus.OK)
  }
}
