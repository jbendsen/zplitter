package dk.lundogbendsen.demo.zplitter.dao


import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PersonRepository : JpaRepository<Person?, Long?> {
    fun findByName(name: String?): List<Person?>?
}