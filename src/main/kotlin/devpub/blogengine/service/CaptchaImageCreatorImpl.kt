package devpub.blogengine.service

import nl.captcha.Captcha
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage

private const val IMAGE_WIDTH = 103
private const val IMAGE_HEIGHT = 56

@Service
open class CaptchaImageCreatorImpl: CaptchaImageCreator {
    override fun create(code: String): BufferedImage {
        val textProducer = { code }

        return Captcha.Builder(IMAGE_WIDTH, IMAGE_HEIGHT)
                .addText(textProducer)
                .addBorder()
                .build()
                .image
    }
}