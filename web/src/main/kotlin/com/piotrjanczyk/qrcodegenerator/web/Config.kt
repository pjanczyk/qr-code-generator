package com.piotrjanczyk.qrcodegenerator.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates.notNull

@Configuration
@ConfigurationProperties(prefix = "qr-code-generator")
class Config {
  var baseUrl by notNull<String>()

  val qrCodeService = QrCodeServiceConfig()

  class QrCodeServiceConfig {
    var host by notNull<String>()
    var port by notNull<Int>()
  }
}
