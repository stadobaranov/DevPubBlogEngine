package devpub.blogengine.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import javax.persistence.TypedQuery
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Order
import javax.persistence.criteria.Root
import kotlin.reflect.KClass

object RepositoryUtils {
    fun <T> loadPage(query: TypedQuery<T>, pageable: Pageable, count: () -> Long): Page<T> {
        if(pageable.isUnpaged) {
            return PageImpl(query.resultList)
        }

        val summaries = query.setFirstResult(pageable.offset.toInt())
                             .setMaxResults(pageable.pageSize)
                             .resultList

        if(summaries.size > 0 && summaries.size < pageable.pageSize) {
            return PageImpl(summaries, pageable, pageable.offset + summaries.size)
        }

        return PageImpl(summaries, pageable, count())
    }

    fun toCountQuery(cb: CriteriaBuilder, entityType: KClass<*>, origin: CriteriaQuery<*>): CriteriaQuery<Long> {
        val countPostQuery = cb.createQuery(Long::class.java)
        val countPost = countPostQuery.from(entityType.java)
        return countPostQuery.select(cb.count(countPost)).where(origin.restriction)
    }

    fun sortOrderToJpa(cb: CriteriaBuilder, root: Root<*>, order: Sort.Order): Order {
        val property = root.get<Any>(order.property)
        return if(order.isAscending) cb.asc(property) else cb.desc(property)
    }
}