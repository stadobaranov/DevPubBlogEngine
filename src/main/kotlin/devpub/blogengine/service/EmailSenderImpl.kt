package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.UserPasswordReset
import devpub.blogengine.service.properties.EmailProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.nio.charset.StandardCharsets

@Service
open class EmailSenderImpl @Autowired constructor(
    private val emailProperties: EmailProperties,
    private val javaEmailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
): EmailSender {
    override fun sendUserPasswordReset(userPasswordReset: UserPasswordReset) {
        val message = javaEmailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(message, StandardCharsets.UTF_8.name())

        messageHelper.setFrom(emailProperties.username)
        messageHelper.setTo(userPasswordReset.userEmail)
        messageHelper.setSubject(ApplicationMessages.USER_PASSWORD_RESET_SUBJECT)

        val templateContext = Context()
        templateContext.setVariable("userName", userPasswordReset.userName)
        templateContext.setVariable("link", userPasswordReset.link)
        messageHelper.setText(templateEngine.process("password-reset.html", templateContext), true)

        javaEmailSender.send(message)
    }
}