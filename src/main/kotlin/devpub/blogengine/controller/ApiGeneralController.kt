package devpub.blogengine.controller

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.model.CommentPostRequest
import devpub.blogengine.model.GlobalSettingsResponse
import devpub.blogengine.model.InitResponse
import devpub.blogengine.model.ModeratePostRequest
import devpub.blogengine.model.PostCountToDatesRequest
import devpub.blogengine.model.PostCountToDatesResponse
import devpub.blogengine.model.TagWeightToNamesRequest
import devpub.blogengine.model.TagWeightToNamesResponse
import devpub.blogengine.model.UpdateGlobalSettingsRequest
import devpub.blogengine.model.UpdateUserProfileWithAvatarRequest
import devpub.blogengine.model.UpdateUserProfileWithoutAvatarRequest
import devpub.blogengine.model.UploadImageRequest
import devpub.blogengine.service.ExceptionHandlingService
import devpub.blogengine.service.GlobalSettingService
import devpub.blogengine.service.ImageUploadService
import devpub.blogengine.service.PostService
import devpub.blogengine.service.TagService
import devpub.blogengine.service.UserProfileService
import devpub.blogengine.service.ValidationErrorsResponseMaker
import devpub.blogengine.service.exception.DuplicateUserEmailException
import devpub.blogengine.service.exception.DuplicateUserNameException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.annotation.PostConstruct
import javax.validation.Valid

@RestController
@RequestMapping("api")
open class ApiGeneralController @Autowired constructor(
    @Value("\${blog-engine.frontend-title}") private val frontendTitle: String,
    @Value("\${blog-engine.frontend-subtitle}") private val frontendSubTitle: String,
    @Value("\${blog-engine.contact-phone}") private val contactPhone: String,
    @Value("\${blog-engine.contact-email}") private val contactEmail: String,
    @Value("\${blog-engine.copyright}") private val copyright: String,
    @Value("\${blog-engine.copyright-from}") private val copyrightFrom: String,
    private val exceptionHandlingService: ExceptionHandlingService,
    private val postService: PostService,
    private val tagService: TagService,
    private val globalSettingService: GlobalSettingService,
    private val userProfileService: UserProfileService,
    private val imageUploadService: ImageUploadService,
    private val validationErrorsResponseMaker: ValidationErrorsResponseMaker
) {
    @GetMapping("init")
    open fun init(): InitResponse {
        return InitResponse(frontendTitle, frontendSubTitle, contactPhone, contactEmail, copyright, copyrightFrom)
    }

    @GetMapping("calendar")
    open fun getPostCountByDate(request: PostCountToDatesRequest): PostCountToDatesResponse {
        return postService.getCountToDates(request)
    }

    @GetMapping("tag")
    open fun getTagWeightToNames(request: TagWeightToNamesRequest): TagWeightToNamesResponse {
        return tagService.getWeightToNames(request)
    }

    @PostMapping("comment")
    open fun comment(@Valid @RequestBody request: CommentPostRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(CommentPostRequest::class, bResult.fieldErrors)
        }

        return postService.comment(request)
    }

    @PostMapping("moderation")
    open fun moderate(@Valid @RequestBody request: ModeratePostRequest) {
        postService.moderate(request)
    }

    @PostConstruct
    open fun registerExceptionHandlerForUploadImage() {
        exceptionHandlingService.register("/api/image") {
            exception, _, _ -> when(exception) {
                is MaxUploadSizeExceededException -> ResponseEntity.badRequest().build<Any>()
                else -> null
            }
        }
    }

    @PostMapping("image")
    open fun uploadImage(@Valid request: UploadImageRequest, bResult: BindingResult): Any {
        if(bResult.hasFieldErrors()) {
            return ResponseEntity.badRequest().build<Any>()
        }

        return imageUploadService.upload(request)
    }

    @GetMapping("settings")
    open fun getGlobalSettings(): GlobalSettingsResponse {
        return globalSettingService.get()
    }

    @PutMapping("settings")
    open fun updateGlobalSettings(@RequestBody request: UpdateGlobalSettingsRequest) {
        globalSettingService.update(request)
    }

    @PostConstruct
    open fun registerExceptionHandlerForUpdateUserProfileWithAvatar() {
        exceptionHandlingService.register("/api/profile/my") {
            exception, _, _ -> when(exception) {
                is MaxUploadSizeExceededException -> ResponseEntity.ok(
                    validationErrorsResponseMaker.make(
                        UpdateUserProfileWithAvatarRequest::class,
                        UpdateUserProfileWithAvatarRequest::avatar.name,
                        ApplicationMessages.MAX_UPLOAD_SIZE_EXCEEDED
                    )
                )
                else -> null
            }
        }
    }

    @PostMapping("profile/my", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    open fun updateUserProfileWithAvatar(
        @Valid request: UpdateUserProfileWithAvatarRequest,
        bResult: BindingResult
    ): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(CommentPostRequest::class, bResult.fieldErrors)
        }

        try {
            return userProfileService.update(request)
        }
        catch(exception: DuplicateUserNameException) {
            return validationErrorsResponseMaker.make(
                UpdateUserProfileWithAvatarRequest::class, UpdateUserProfileWithAvatarRequest::name.name, exception.message!!
            )
        }
        catch(exception: DuplicateUserEmailException) {
            return validationErrorsResponseMaker.make(
                UpdateUserProfileWithAvatarRequest::class, UpdateUserProfileWithAvatarRequest::email.name, exception.message!!
            )
        }
    }

    @PostMapping("profile/my", consumes = [MediaType.APPLICATION_JSON_VALUE])
    open fun updateUserProfileWithoutAvatar(
        @Valid @RequestBody request: UpdateUserProfileWithoutAvatarRequest,
        bResult: BindingResult
    ): Any {
        if(bResult.hasFieldErrors()) {
            return validationErrorsResponseMaker.make(CommentPostRequest::class, bResult.fieldErrors)
        }

        try {
            return userProfileService.update(request)
        }
        catch(exception: DuplicateUserNameException) {
            return validationErrorsResponseMaker.make(
                UpdateUserProfileWithoutAvatarRequest::class, UpdateUserProfileWithoutAvatarRequest::name.name, exception.message!!
            )
        }
        catch(exception: DuplicateUserEmailException) {
            return validationErrorsResponseMaker.make(
                UpdateUserProfileWithoutAvatarRequest::class, UpdateUserProfileWithoutAvatarRequest::email.name, exception.message!!
            )
        }
    }
}