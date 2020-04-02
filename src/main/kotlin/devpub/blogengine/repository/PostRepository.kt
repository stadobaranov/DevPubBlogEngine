package devpub.blogengine.repository

import devpub.blogengine.model.entity.Post
import org.springframework.stereotype.Repository

@Repository
interface PostRepository: BaseRepository<Post>, PostRepositoryCustom