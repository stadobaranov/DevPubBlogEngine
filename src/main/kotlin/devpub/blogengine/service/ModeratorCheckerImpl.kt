package devpub.blogengine.service

import devpub.blogengine.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class ModeratorCheckerImpl @Autowired constructor(
    private val userRepository: UserRepository
): ModeratorChecker {
    @Transactional(readOnly = true)
    override fun check(userId: Int): Boolean? {
        return userRepository.checkIsModerator(userId)
    }
}