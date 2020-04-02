package devpub.blogengine.service

import devpub.blogengine.model.CommentPostRequest
import devpub.blogengine.model.CommentPostResponse
import devpub.blogengine.model.DetailedPostResponse
import devpub.blogengine.model.ModeratePostRequest
import devpub.blogengine.model.PostCountToDatesRequest
import devpub.blogengine.model.PostCountToDatesResponse
import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.SavePostRequest
import devpub.blogengine.model.VotePostRequest

interface PostService {
    fun getDetails(id: Int): DetailedPostResponse

    fun getCountToDates(request: PostCountToDatesRequest): PostCountToDatesResponse

    fun create(request: SavePostRequest): ResultResponse

    fun update(id: Int, request: SavePostRequest): ResultResponse

    fun comment(request: CommentPostRequest): CommentPostResponse

    fun moderate(request: ModeratePostRequest)

    fun like(request: VotePostRequest): ResultResponse

    fun dislike(request: VotePostRequest): ResultResponse
}