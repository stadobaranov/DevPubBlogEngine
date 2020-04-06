package devpub.blogengine.service

import devpub.blogengine.model.TagWeightToNamesRequest
import devpub.blogengine.model.TagWeightToNamesResponse

interface TagService {
    fun getWeightToNames(request: TagWeightToNamesRequest): TagWeightToNamesResponse
}