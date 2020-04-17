package devpub.blogengine.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
open class DefaultController {
    @GetMapping("/")
    open fun index() = "forward:/index.html"
}