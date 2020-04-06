package devpub.blogengine.service

import devpub.blogengine.model.ResultResponse
import devpub.blogengine.model.UpdateUserProfileRequest
import devpub.blogengine.service.aspect.Authorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class UserProfileServiceImpl @Autowired constructor(
    private val userService: UserService,
    private val uploadStorage: UploadStorage
): UserProfileService {
    @Authorized
    override fun update(request: UpdateUserProfileRequest): ResultResponse {
        var avatar: String? = null
        var avatarUrl: String? = null
        val avatarFile = request.avatar

        if(avatarFile != null && !request.removeAvatar) {
            avatar = uploadStorage.store(avatarFile)
            avatarUrl = UploadStorage.getUrl(avatar)
        }

        try {
            userService.update(request.name, request.email, request.password, request.removeAvatar, avatarUrl)
        }
        catch(exception: Exception) {
            if(avatar != null) {
                try {
                    uploadStorage.remove(avatar)
                }
                catch(suppressed: Exception) {
                    exception.addSuppressed(suppressed)
                }
            }

            throw exception
        }

        return ResultResponse.of(true)
    }
}