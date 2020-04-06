package devpub.blogengine.service

import devpub.blogengine.model.TagWeightToNamesRequest
import devpub.blogengine.model.TagWeightToNamesResponse
import devpub.blogengine.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Float.min
import java.time.LocalDateTime

@Service
open class TagServiceImpl @Autowired constructor(
    private val tagRepository: TagRepository
): TagService {
    @Transactional(readOnly = true)
    override fun getWeightToNames(request: TagWeightToNamesRequest): TagWeightToNamesResponse {
        val now = LocalDateTime.now()
        val postCountToNames = tagRepository.findAllPostCountToNames(request.query, now)

        if(postCountToNames.isEmpty()) {
            return TagWeightToNamesResponse(emptyList())
        }

        val maxPostCount: Int

        if(request.query.isEmpty()) {
            maxPostCount = postCountToNames.maxBy { it.count }!!.count
        }
        else {
            maxPostCount = tagRepository.findMaxPostCountGroupedByName(now).toInt()
        }

        return TagWeightToNamesResponse(
            postCountToNames.map {
                return@map TagWeightToNamesResponse.Entry(
                    it.name.value,
                    min(it.count.toFloat() / maxPostCount, 1f)
                )
            }
        )
    }
}