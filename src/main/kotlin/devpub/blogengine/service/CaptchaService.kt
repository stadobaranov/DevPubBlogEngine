package devpub.blogengine.service

import devpub.blogengine.model.GenerateCaptchaResponse

interface CaptchaService {
    fun generate(): GenerateCaptchaResponse

    fun process(code: String, secretCode: String)
}