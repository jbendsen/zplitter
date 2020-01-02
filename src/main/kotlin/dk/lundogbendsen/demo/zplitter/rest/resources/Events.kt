package dk.lundogbendsen.demo.zplitter.rest.resources

import dk.lundogbendsen.demo.zplitter.dao.EventRepository
import dk.lundogbendsen.demo.zplitter.rest.model.EventModel
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.rest.model.PersonModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("api/events")
class Events {

    @Autowired
    lateinit var repo: EventRepository

    @Transactional
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody event: EventModel): ResponseEntity<EventModel> {
        if (event.id != null) {
            return ResponseEntity(HttpStatus.EXPECTATION_FAILED)
        }
        val saved = repo.save(Event(null, event.name))
        return ResponseEntity(EventModel(saved), HttpStatus.CREATED)
    }


    @Transactional
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): List<EventModel> {
        val list = repo.findAll()
        return list.map { it ->
            getEventModel(it)

        }
    }

    @Transactional
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<EventModel> {
        val event: Optional<Event?> = repo.findById(id)
        if (event.isPresent) {
            return ResponseEntity(getEventModel(event.get()), HttpStatus.FOUND);
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    private fun getPersonModel(id: Long?, name: String): PersonModel {
        val personModel = PersonModel(id, name)
        if (id != null) {
            val linkToSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Persons::class.java).findById(id)).withSelfRel()
            personModel.add(linkToSelf)
        }
        return personModel
    }

    private fun getEventModel(event: Event?): EventModel {
        if (event == null) throw NullPointerException("event must not be null!")
        val eventModel = EventModel(event.id, event.name)
        eventModel.persons.addAll(event.persons.map { person ->
            getPersonModel(person.id, person.name)
        })
        if (event.id != null) {
            val linkToSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Events::class.java).findById(event.id)).withSelfRel()
            eventModel.add(linkToSelf)
        }
        return eventModel
    }
}