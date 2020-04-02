package devpub.blogengine.model.validation

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

private val availableTags = arrayOf(
    "strong", "em", "b", "i", "p", "code", "pre", "tt", "samp", "kbd", "var", "sub", "sup", "dfn", "cite",
    "big", "small", "address", "hr", "br", "id", "div", "span", "h1", "h2", "h3", "h4", "h5", "h6", "ul",
    "ol", "li", "dl", "dt", "dd", "abbr", "acronym", "a", "img", "blockquote", "del", "ins", "u", "table",
    "thead", "tbody", "tfoot", "tr", "th", "td", "colgroup"
)

private val availableTagAttributes = arrayOf(
    "href", "src", "width", "height", "alt", "cite", "datetime", "title", "class", "name", "abbr", "target",
    "border", "style"
)

private val joinedTags = availableTags.joinToString("|")
private val joinedTagAttributes = availableTagAttributes.joinToString("|")

private val safeHtmlPattern = Pattern.compile(
    "^(?:[^<]+|(?:</?(?:$joinedTags)(?:\\s+(?:$joinedTagAttributes)\\s*=\\s*(?:(?:\"[^\"]*\")|(?:'[^']*')|[^\"'<>\\s]+))*\\s*>))*$"
)

open class SafeHtmlValidator: ConstraintValidator<SafeHtml, String> {
    override fun isValid(html: String?, context: ConstraintValidatorContext): Boolean {
        if(html == null) {
            return true
        }

        return safeHtmlPattern.matcher(html).matches()
    }
}