package devpub.blogengine.repository

import devpub.blogengine.model.entity.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: BaseRepository<User>