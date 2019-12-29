package dk.lundogbendsen.demo.zplitter.dao


import dk.lundogbendsen.demo.zplitter.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import java.util.*


interface EventRepository : JpaRepository<Event?, Long?> {
    fun findByName(name: String?): List<Event?>?
    override fun findById(id: Long): Optional<Event?>
}