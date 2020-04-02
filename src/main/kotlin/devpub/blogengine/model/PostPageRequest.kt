package devpub.blogengine.model

import devpub.blogengine.model.request.PropertyAlias

class PostPageRequest: PageRequest() {
    var order = PostOrder.RECENT

    @Deprecated("Параметр для запроса")
    var mode by PropertyAlias(this::order)
}