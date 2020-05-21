package com.piotrjanczyk.qrcodegenerator.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QrCodeGeneratorApplication

fun main(args: Array<String>) {
	runApplication<QrCodeGeneratorApplication>(*args)
}
