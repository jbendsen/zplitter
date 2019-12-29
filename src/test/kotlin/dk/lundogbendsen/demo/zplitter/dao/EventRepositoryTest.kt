package dk.lundogbendsen.demo.zplitter.dao


import dk.lundogbendsen.demo.zplitter.model.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.persistence.EntityManager
import javax.sql.DataSource


@ExtendWith(SpringExtension::class)
@DataJpaTest
internal class EventRepositoryTest {
    @Autowired
    private val dataSource: DataSource? = null
    @Autowired
    private val jdbcTemplate: JdbcTemplate? = null
    @Autowired
    private val entityManager: EntityManager? = null
    @Autowired
    private val eventRepository: EventRepository? = null

    @Test
    fun injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull()
        assertThat(jdbcTemplate).isNotNull()
        assertThat(entityManager).isNotNull()
        assertThat(eventRepository).isNotNull()
    }

    @Test
    fun createAndFind() {
        val size = eventRepository?.findAll()?.size
        val event = eventRepository?.save(Event(null, "test event"))
        assertThat(event).isNotNull()
        assertThat(event?.id).isNotNull()
        assert(size?.plus(1) == eventRepository?.findAll()?.size)
        val findByName = eventRepository?.findByName("test event")
        assertThat(findByName).isNotNull()
    }
}