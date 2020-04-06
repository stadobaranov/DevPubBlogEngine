package devpub.blogengine

import devpub.blogengine.model.entity.Post
import devpub.blogengine.model.entity.PostComment

object ApplicationMessages {
    const val BAD_REQUEST = "Некорректный запрос"

    const val ANNOUNCED_TEXT_IS_EMPTY = "Анонсируемый текст пуст"
    const val ANNOUNCE_NOT_MATCHED = "Анонс не найден"
    const val ANNOUNCE_IS_TOO_SHORT = "Анонс слишком короткий"

    const val UNAUTHORIZED = "Доступ запрещен"

    const val FIELD_IS_REQUIRED = "Необходимо для заполнения"
    const val INVALID_ID = "Недопустимый идентификатор"
    const val INVALID_TAG_NAMES = "Недопустимые названия тегов"
    const val INVALID_ANNOUNCE = "Не содержит анонс"
    const val INVALID_HTML = "Недопустимый HTML"
    const val INVALID_MIN_POST_TITLE_LENGTH = "Минимально допустимая длина - ${Post.MIN_TITLE_LENGTH} символов"
    const val INVALID_MAX_POST_TITLE_LENGTH = "Максимально допустимая длина - ${Post.MAX_TITLE_LENGTH} символов"
    const val INVALID_MIN_POST_TEXT_LENGTH = "Минимально допустимая длина - ${Post.MIN_TEXT_LENGTH} символов"
    const val INVALID_MAX_POST_TEXT_LENGTH = "Максимально допустимая длина - ${Post.MAX_TEXT_LENGTH} символов"
    const val INVALID_MAX_POST_COMMENT_TEXT_LENGTH = "Максимально допустимая длина - ${PostComment.MAX_TEXT_LENGTH} символов"
    const val INVALID_IMAGE = "Недопустимое изображение"

    const val POST_NOT_FOUND = "Пост не найден"
    const val USER_NOT_FOUND = "Пользователь не найден"

    const val MISSING_POST = "Пост не существует"
    const val MISSING_POST_AUTHOR = "Автор поста не сущестует"
    const val MISSING_COMMENT_AUTHOR = "Автор комментария не сущестует"
    const val MISSING_PARENT_COMMENT = "Родительский комментарий не сущестует"
    const val MISSING_POST_MODERATOR = "Модератор поста не сущестует"
    const val MISSING_POST_VOTER = "Голосующий не существует"

    const val DUPLICATE_USER_NAME = "Имя пользователя уже существует"
    const val DUPLICATE_USER_EMAIL = "Адрес эл. почты уже существует"

    const val USER_PASSWORD_RESET_SUBJECT = "Восстановление пароля"
    const val USER_RESET_CODE_EXPIRED = "Код восстановления устарел"

    const val CAPTCHA_EXPIRED = "Капча устарела"
    const val INVALID_CAPTCHA_CODE = "Неправильный код"

    const val INVALID_GLOBAL_SETTING_VALUE = "Недопустимое значение глобальной настройки"
}