package devpub.blogengine.controller

import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder

@ControllerAdvice
open class DefaultControllerAdvice {
    @InitBinder
    open fun initDataBinder(dataBinder: WebDataBinder) {
        dataBinder.registerCustomEditor(String::class.java, StringTrimmerEditor(false))
    }
}