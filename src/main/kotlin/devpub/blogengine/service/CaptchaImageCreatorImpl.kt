package devpub.blogengine.service

import nl.captcha.Captcha
import nl.captcha.text.producer.TextProducer
import nl.captcha.text.renderer.DefaultWordRenderer
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.util.Collections

private const val IMAGE_WIDTH = 103
private const val IMAGE_HEIGHT = 56
private val colors = Collections.singletonList(Color.BLACK)
private val fonts = Collections.singletonList(Font("Arial", Font.BOLD, 32))

@Service
open class CaptchaImageCreatorImpl: CaptchaImageCreator {
    override fun create(code: String): BufferedImage {
        return Captcha.Builder(IMAGE_WIDTH, IMAGE_HEIGHT)
                .addText(TextProducer { code }, DefaultWordRenderer(colors, fonts))
                .addBorder()
                .build()
                .image
    }
}