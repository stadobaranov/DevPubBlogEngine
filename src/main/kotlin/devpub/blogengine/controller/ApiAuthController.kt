package devpub.blogengine.controller

import devpub.blogengine.model.AuthorizedUserResponse
import devpub.blogengine.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
open class ApiAuthController @Autowired constructor(
    private val userService: UserService
) {
    @GetMapping("check")
    open fun checkUserLoggedIn(): AuthorizedUserResponse {
        return userService.checkLoggedIn()
    }
}