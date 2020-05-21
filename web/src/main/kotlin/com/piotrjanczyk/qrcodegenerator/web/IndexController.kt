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
    @RequestParam("data", defaultValue = "") data: String,
    model: Model
  ): String {
    model.addAttribute("data", data)
    if (data.isNotBlank()) {
      val url = "${config.baseUrl}/qr?data=${UriUtils.encodeQueryParam(data, Charsets.UTF_8)}"
      model.addAttribute("url", url)
    }
    return "index"
  }
}
