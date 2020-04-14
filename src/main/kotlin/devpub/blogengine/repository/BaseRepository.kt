package devpub.blogengine.repository

import devpub.blogengine.model.entity.Persistent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

@NoRepositoryBean
interface BaseRepository<T: Persistent>: JpaRepository<T, Int> {
    @Query("""
        select count(e.id) > 0
        from #{#entityName} e
        where e.id = :id
    """)
    fun checkExisting(@Param("id") id: Int): Boolean

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
        select e 
        from #{#entityName} e
        where e.id = :id
    """)
    fun findAndLockForShare(@Param("id") id: Int): T?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select e 
        from #{#entityName} e
        where e.id = :id
    """)
    fun findAndLockForUpdate(@Param("id") id: Int): T?

    @Modifying
    @Query("""
        delete 
        from #{#entityName} e
        where e.id = :id
    """)
    fun deleteOne(@Param("id") id: Int): Int

    @Modifying
    @Query("""
        delete 
        from #{#entityName} e
        where e.id in :ids
    """)
    fun deleteAll(@Param("ids") ids: Set<Int>): Int
}