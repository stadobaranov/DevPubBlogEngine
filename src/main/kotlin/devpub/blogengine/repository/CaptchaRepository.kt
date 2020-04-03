package devpub.blogengine.repository

import devpub.blogengine.model.entity.Captcha
import org.springframework.stereotype.Repository

@Repository
interface CaptchaRepository: BaseRepository<Captcha>