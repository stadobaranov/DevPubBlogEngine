package devpub.blogengine.repository

import devpub.blogengine.model.entity.User

interface UserRepositoryCustom {
    fun findByNameOrEmail(name: String?, email: String?, exceptUserId: Int? = null): User?
}