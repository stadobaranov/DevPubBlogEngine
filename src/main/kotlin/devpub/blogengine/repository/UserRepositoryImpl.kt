package devpub.blogengine.repository

import devpub.blogengine.model.entity.User
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Predicate

open class UserRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
): UserRepositoryCustom {
    override fun findByNameOrEmail(name: String?, email: String?, exceptUserId: Int?): User? {
        if(name == null && email == null) {
            return null
        }

        val cb = entityManager.criteriaBuilder

        val userQuery = cb.createQuery(User::class.java)
        val user = userQuery.from(User::class.java)

        var predicate: Predicate? = null

        if(name != null) {
            predicate = cb.equal(user.get<String>("name"), name)
        }

        if(email != null) {
            val emailEquals = cb.equal(user.get<String>("email"), email)

            if(predicate == null) {
                predicate = emailEquals
            }
            else {
                predicate = cb.or(predicate, emailEquals)
            }
        }

        if(exceptUserId != null) {
            predicate = cb.and(cb.notEqual(user.get<Int>("id"), exceptUserId), predicate)
        }

        try {
            return entityManager.createQuery(userQuery.where(predicate))
                                .setMaxResults(1)
                                .singleResult
        }
        catch(exception: NoResultException) {
            return null
        }
    }
}