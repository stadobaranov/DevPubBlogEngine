package devpub.blogengine.service

import devpub.blogengine.ApplicationMessages
import devpub.blogengine.service.exception.AnnounceExtractionException
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import kotlin.math.min

private const val MIN_ANNOUNCE_LENGTH = 150
private const val MAX_ANNOUNCE_LENGTH = 1000

private const val CUT_LINE = "..."

private const val TAG_REGEX = "</?(?:abbr|b|i|em|q|sub|sup|code|dfn|time|span|br)[^>]*>"
private const val ANNOUNCE_REGEX = "(?:[^<]+|(?:$TAG_REGEX))+"

private val lastWhitespacePattern = Pattern.compile("(?:\\s*\\S+)*(\\s)")

private val firstParagraphPattern = Pattern.compile(
    "(?<=^<(p|div)>)$ANNOUNCE_REGEX(?=</\\1>)|^$ANNOUNCE_REGEX(?:(?=<(?:p|div|h[1-6]|[uo]l)>)|$)"
)

private val tagPattern = Pattern.compile(TAG_REGEX)

@Service
open class AnnounceExtractorImpl: AnnounceExtractor {
    override fun extract(text: String): String {
        if(text.isEmpty()) {
            throw AnnounceExtractionException(ApplicationMessages.ANNOUNCED_TEXT_IS_EMPTY)
        }

        val firstParagraphMatcher = firstParagraphPattern.matcher(text)

        if(!firstParagraphMatcher.find()) {
            throw AnnounceExtractionException(ApplicationMessages.ANNOUNCE_NOT_MATCHED)
        }

        val announce = tagPattern.matcher(firstParagraphMatcher.group()).replaceAll("")

        if(announce.length < MIN_ANNOUNCE_LENGTH) {
            throw AnnounceExtractionException(ApplicationMessages.ANNOUNCE_IS_TOO_SHORT)
        }
        else if(announce.length > MAX_ANNOUNCE_LENGTH) {
            return cut(announce)
        }

        return announce
    }

    private fun cut(announce: String): String {
        val lastWhitespaceMatcher = lastWhitespacePattern.matcher(announce)

        if(!lastWhitespaceMatcher.find()) {
            return appendCutLine(announce, announce.length)
        }

        val lastWhitespacePos = lastWhitespaceMatcher.start(1)

        if(lastWhitespacePos < MIN_ANNOUNCE_LENGTH) {
            return appendCutLine(announce, announce.length)
        }

        return appendCutLine(announce, lastWhitespacePos)
    }

    private fun appendCutLine(announce: String, cutLineStart: Int): String {
        return announce.substring(0, min(cutLineStart, MAX_ANNOUNCE_LENGTH - CUT_LINE.length)) + CUT_LINE
    }
}