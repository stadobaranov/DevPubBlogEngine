package devpub.blogengine.service

import java.awt.image.BufferedImage

interface CaptchaImageCreator {
    fun create(code: String): BufferedImage
}