package dk.lundogbendsen.demo.zplitter.rest.resources

import dk.lundogbendsen.demo.zplitter.dao.EventRepository
import dk.lundogbendsen.demo.zplitter.rest.model.EventModel
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.rest.model.PersonModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/events")
class Events {

    @Autowired
    lateinit var repo: EventRepository

    @Value("\${application.name}")
    var appname = "n/a"

    @RequestMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): String {
        return "Hello, $name, $appname"
    }

    @Transactional
    @PostMapping(consumes=[MediaType.APPLICATION_JSON_VALUE], produces=[MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody event : EventModel) : ResponseEntity<EventModel> {
        if (event.id!=null) {
            return ResponseEntity(HttpStatus.EXPECTATION_FAILED)
        }
        val saved = repo.save(Event(null, event.name))
        return ResponseEntity(EventModel(saved), HttpStatus.CREATED)
    }


    @Transactional
    @GetMapping(produces=[MediaType.APPLICATION_JSON_VALUE])
    fun list() : List<EventModel> {
        val list = repo.findAll()
        return list.map {it ->
            val event = EventModel(it?.id, it!!.name)
            it.persons.map {
                person -> PersonModel(person.id, person.name)
            }
            event

        }
    }

    @Transactional
    @GetMapping("/{id}", produces=[MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable("id") id : Long) : EventModel {
        return EventModel(repo.findById(id).get());
    }

}