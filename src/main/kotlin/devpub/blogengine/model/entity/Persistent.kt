package devpub.blogengine.model.entity

import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class Persistent protected constructor(id: Int = 0): Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id = id
}