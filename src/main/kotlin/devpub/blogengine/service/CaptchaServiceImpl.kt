package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.GenerateCaptchaResponse
import devpub.blogengine.model.entity.Captcha
import devpub.blogengine.repository.CaptchaRepository
import devpub.blogengine.service.exception.CaptchaExpiredException
import devpub.blogengine.service.exception.InvalidCaptchaCodeException
import devpub.blogengine.service.properties.CaptchaProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.Base64
import javax.imageio.ImageIO

private const val CAPTCHA_IMAGE_FORMAT = "png"

@Service
open class CaptchaServiceImpl @Autowired constructor(
    private val captchaProperties: CaptchaProperties,
    private val randomStringGenerator: RandomStringGenerator,
    private val captchaImageCreator: CaptchaImageCreator,
    private val captchaRepository: CaptchaRepository
): CaptchaService {
    private fun isExpired(createdAt: LocalDateTime): Boolean {
        return createdAt.plusMinutes(captchaProperties.lifetime) < LocalDateTime.now()
    }

    @Transactional
    override fun generate(): GenerateCaptchaResponse {
        deleteExpiredCodes()

        val code = randomStringGenerator.generate(Captcha.MIN_CODE_LENGTH, Captcha.MAX_CODE_LENGTH)
        val secretCode = randomStringGenerator.generate(Captcha.SECRET_CODE_LENGTH)

        val captcha = Captcha()
        captcha.code = code
        captcha.secretCode = secretCode
        captcha.createdAt = LocalDateTime.now()

        repeatIfThrown(DataIntegrityViolationException::class) {
            captchaRepository.saveAndFlush(captcha)
        }

        return GenerateCaptchaResponse(secretCode, createAndEncodeImage(code))
    }

    private fun deleteExpiredCodes() {
        captchaRepository.deleteBefore(LocalDateTime.now().minusMinutes(captchaProperties.lifetime))
    }

    private fun createAndEncodeImage(code: String): String {
        ByteArrayOutputStream().use {
            ImageIO.write(captchaImageCreator.create(code), CAPTCHA_IMAGE_FORMAT, it)
            return "data:image/$CAPTCHA_IMAGE_FORMAT;base64,${Base64.getEncoder().encodeToString(it.toByteArray())}"
        }
    }

    @Transactional(noRollbackFor = [CaptchaExpiredException::class])
    override fun process(code: String, secretCode: String) {
        val captcha = captchaRepository.findBySecretCode(secretCode)

        if(captcha == null) {
            throw CaptchaExpiredException(ApplicationMessages.CAPTCHA_EXPIRED)
        }
        else if(isExpired(captcha.createdAt)) {
            captchaRepository.deleteOne(captcha.id)
            throw CaptchaExpiredException(ApplicationMessages.CAPTCHA_EXPIRED)
        }
        else if(!captcha.code.equals(code, true)) {
            throw InvalidCaptchaCodeException(ApplicationMessages.INVALID_CAPTCHA_CODE)
        }
        else if(captchaRepository.deleteOne(captcha.id) == 0) {
            throw CaptchaExpiredException(ApplicationMessages.CAPTCHA_EXPIRED)
        }
    }
}