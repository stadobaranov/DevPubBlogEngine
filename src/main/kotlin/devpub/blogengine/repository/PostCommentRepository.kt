package devpub.blogengine.repository

import devpub.blogengine.model.entity.PostComment
import org.springframework.stereotype.Repository

@Repository
interface PostCommentRepository: BaseRepository<PostComment>