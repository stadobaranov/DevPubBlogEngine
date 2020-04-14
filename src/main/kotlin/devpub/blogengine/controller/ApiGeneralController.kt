package devpub.blogengine.controller

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.controller.validation.buildMapper
import devpub.blogengine.model.CommentPostRequest
import devpub.blogengine.model.GlobalSettingsResponse
import devpub.blogengine.model.InitResponse
import devpub.blogengine.model.ModeratePostRequest
import devpub.blogengine.model.PostCountToDatesRequest
import devpub.blogengine.model.PostCountToDatesResponse
import devpub.blogengine.model.PostStatisticsResponse
import devpub.blogengine.model.TagWeightToNamesRequest
import devpub.blogengine.model.TagWeightToNamesResponse
import devpub.blogengine.model.UpdateGlobalSettingsRequest
import devpub.blogengine.model.UpdateUserProfileWithAvatarRequest
import devpub.blogengine.model.UpdateUserProfileWithoutAvatarRequest
import devpub.blogengine.model.UploadImageRequest
import devpub.blogengine.service.GlobalSettingService
import devpub.blogengine.service.ImageUploadService
import devpub.blogengine.service.MaxUploadSizeExceededExceptionHandlingService
import devpub.blogengine.service.PostService
import devpub.blogengine.service.PostStatisticsService
import devpub.blogengine.service.TagService
import devpub.blogengine.service.UserProfileService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import devpub.blogengine.service.exception.DuplicateUserEmailException
import devpub.blogengine.service.exception.DuplicateUserNameException
import devpub.blogengine.service.properties.FrontendProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct
import javax.validation.Valid

private const val UPLOAD_IMAGE_URL = "/api/image"
private const val UPDATE_USER_PROFILE_URL = "/api/profile/my"

@RestController
open class ApiGeneralController @Autowired constructor(
    private val frontendProperties: FrontendProperties,
    private val maxUploadSizeExceededExceptionHandlingService: MaxUploadSizeExceededExceptionHandlingService,
    private val postService: PostService,
    private val tagService: TagService,
    private val globalSettingService: GlobalSettingService,
    private val userProfileService: UserProfileService,
    private val imageUploadService: ImageUploadService,
    private val postStatisticsService: PostStatisticsService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
) {
    @GetMapping("/api/init")
    open fun init(): InitResponse {
        return InitResponse(
            frontendProperties.title,
            frontendProperties.subTitle,
            frontendProperties.contactPhone,
            frontendProperties.contactEmail,
            frontendProperties.copyright,
            frontendProperties.copyrightFrom
        )
    }

    @GetMapping("/api/calendar")
    open fun getPostCountByDate(request: PostCountToDatesRequest): PostCountToDatesResponse {
        return postService.getCountToDates(request)
    }

    @GetMapping("/api/tag")
    open fun getTagWeightToNames(request: TagWeightToNamesRequest): TagWeightToNamesResponse {
        return tagService.getWeightToNames(request)
    }

    @PostMapping("/api/comment")
    open fun comment(@Valid @RequestBody request: CommentPostRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.makeEntity(request.javaClass.kotlin, bResult.fieldErrors)
        }

        return postService.comment(request)
    }

    @PostMapping("/api/moderation")
    open fun moderate(@Valid @RequestBody request: ModeratePostRequest) {
        postService.moderate(request)
    }

    @PostConstruct
    open fun registerExceptionHandlerForUploadImage() {
        maxUploadSizeExceededExceptionHandlingService.registerSimpleHandler("post", UPLOAD_IMAGE_URL) {
            return@registerSimpleHandler ResponseEntity.badRequest().build<Any>()
        }
    }

    @PostMapping(UPLOAD_IMAGE_URL)
    open fun uploadImage(@Valid request: UploadImageRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().build<Any>()
        }

        return imageUploadService.upload(request)
    }

    @GetMapping("/api/settings")
    open fun getGlobalSettings(): GlobalSettingsResponse {
        return globalSettingService.get()
    }

    @PutMapping("/api/settings")
    open fun updateGlobalSettings(@RequestBody request: UpdateGlobalSettingsRequest) {
        globalSettingService.update(request)
    }

    @PostConstruct
    open fun registerExceptionHandlerForUpdateUserProfileWithAvatar() {
        maxUploadSizeExceededExceptionHandlingService.registerSimpleHandler("post", UPDATE_USER_PROFILE_URL) {
            return@registerSimpleHandler validationErrorsResponseMaker.makeEntity(
                UpdateUserProfileWithAvatarRequest::class,
                UpdateUserProfileWithAvatarRequest::avatar,
                ApplicationMessages.MAX_UPLOAD_SIZE_EXCEEDED
            )
        }
    }

    @PostMapping(UPDATE_USER_PROFILE_URL, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    open fun updateUserProfileWithAvatar(
        @Valid request: UpdateUserProfileWithAvatarRequest,
        bResult: BindingResult
    ): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.makeEntity(request.javaClass.kotlin, bResult.fieldErrors)
        }

        try {
            return userProfileService.update(request)
        }
        catch(exception: Exception) {
            return validationErrorsResponseMaker.buildMapper(request.javaClass.kotlin)
                .map(DuplicateUserNameException::class to UpdateUserProfileWithAvatarRequest::name)
                .map(DuplicateUserEmailException::class to UpdateUserProfileWithAvatarRequest::email)
                .makeResponseEntity(exception)
        }
    }

    @PostMapping(UPDATE_USER_PROFILE_URL, consumes = [MediaType.APPLICATION_JSON_VALUE])
    open fun updateUserProfileWithoutAvatar(
        @Valid @RequestBody request: UpdateUserProfileWithoutAvatarRequest,
        bResult: BindingResult
    ): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.makeEntity(request.javaClass.kotlin, bResult.fieldErrors)
        }

        try {
            return userProfileService.update(request)
        }
        catch(exception: Exception) {
            return validationErrorsResponseMaker.buildMapper(request.javaClass.kotlin)
                .map(DuplicateUserNameException::class to UpdateUserProfileWithoutAvatarRequest::name)
                .map(DuplicateUserEmailException::class to UpdateUserProfileWithoutAvatarRequest::email)
                .makeResponseEntity(exception)
        }
    }

    @GetMapping("/api/statistics/all")
    open fun getPostStatisticsForAll(): PostStatisticsResponse {
        return postStatisticsService.getForAll()
    }

    @GetMapping("/api/statistics/my")
    open fun getPostStatisticsForCurrentUser(): PostStatisticsResponse {
        return postStatisticsService.getForCurrentUser()
    }
}