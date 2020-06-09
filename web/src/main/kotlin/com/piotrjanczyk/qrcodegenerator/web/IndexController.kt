package com.piotrjanczyk.qrcodegenerator.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.util.UriUtils

@Controller
class IndexController(
  private val config: Config
) {
  @GetMapping("/")
  fun home(
    @RequestParam(required = false) data: String?,
    @RequestParam(required = false) imageFormat: ImageFormat?,
    @RequestParam(required = false) errorCorrection: ErrorCorrection?,
    @RequestParam(required = false) version: Int?,
    @RequestParam(required = false) foregroundColor: String?,
    @RequestParam(required = false) backgroundColor: String?,
    @RequestParam(required = false) boxSize: Int?,
    @RequestParam(required = false) border: Int?,
    model: Model
  ): String {
    val definition = QrCodeDefinition.build(
      data = data ?: "",
      imageFormat = imageFormat,
      errorCorrection = errorCorrection,
      version = version,
      foregroundColor = foregroundColor,
      backgroundColor = backgroundColor,
      boxSize = boxSize,
      border = border
    )
    model.addAttribute("definition", definition)
    if (definition.data.isNotBlank()) {
      model.addAttribute("url", buildImageUrl(definition))
    }
    return "index"
  }

  fun buildImageUrl(definition: QrCodeDefinition): String {
    var url = "${config.baseUrl}/qr"
    url += "?data=${UriUtils.encodeQueryParam(definition.data, Charsets.UTF_8)}"
    url += "&format=${definition.imageFormat}"
    if (definition.hasCustomErrorCorrection) {
      url += "&ec=${definition.errorCorrection}"
    }
    if (definition.hasCustomVersion) {
      url += "&version=${definition.version}"
    }
    if (definition.hasCustomForegroundColor) {
      url += "&fg=${UriUtils.encodeQueryParam(definition.foregroundColor, Charsets.UTF_8)}"
    }
    if (definition.hasCustomBackgroundColor) {
      url += "&bg=${UriUtils.encodeQueryParam(definition.backgroundColor, Charsets.UTF_8)}"
    }
    if (definition.hasCustomBoxSize) {
      url += "&box=${definition.boxSize}"
    }
    if (definition.hasCustomBorder) {
      url += "&border=${definition.border}"
    }
    return url
  }
}
