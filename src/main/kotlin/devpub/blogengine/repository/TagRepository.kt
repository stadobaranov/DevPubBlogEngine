package devpub.blogengine.repository

import devpub.blogengine.model.entity.Tag
import org.springframework.stereotype.Repository

@Repository
interface TagRepository: BaseRepository<Tag>