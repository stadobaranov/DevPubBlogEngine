package devpub.blogengine.repository

import devpub.blogengine.model.entity.GlobalSetting
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GlobalSettingRepository: BaseRepository<GlobalSetting> {
    @Query("""
        select gs
        from GlobalSetting gs
        where gs.code = :code
    """)
    fun findByCode(@Param("code") code: GlobalSetting.Code): GlobalSetting?

    @Modifying
    @Query("""
        update GlobalSetting gs
        set gs.value = :value
        where gs.code = :code
    """)
    fun update(
        @Param("code") code: GlobalSetting.Code,
        @Param("value") value: String
    ): Int
}