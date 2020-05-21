package com.piotrjanczyk.qrcodegenerator.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class QrController(
  private val qrCodeServiceClient: QrCodeServiceClient
) {
  @GetMapping("/qr", produces = [MediaType.IMAGE_PNG_VALUE])
  @ResponseBody
  suspend fun getCode(
    @RequestParam("data") data: String
  ): ByteArray {
    return qrCodeServiceClient.createQrCode(data)
  }
}
