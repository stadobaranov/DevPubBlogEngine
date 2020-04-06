package devpub.blogengine.service

import devpub.blogengine.model.UserPasswordReset

interface EmailSender {
    fun sendUserPasswordReset(userPasswordReset: UserPasswordReset)
}