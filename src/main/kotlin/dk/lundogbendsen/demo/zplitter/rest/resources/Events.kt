package dk.lundogbendsen.demo.zplitter.rest.resources

import dk.lundogbendsen.demo.zplitter.dao.EventRepository
import dk.lundogbendsen.demo.zplitter.dao.ExpenseRepository
import dk.lundogbendsen.demo.zplitter.dao.PersonRepository
import dk.lundogbendsen.demo.zplitter.rest.model.EventModel
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import dk.lundogbendsen.demo.zplitter.rest.dto.EventDto
import dk.lundogbendsen.demo.zplitter.rest.dto.ExpenseDto
import dk.lundogbendsen.demo.zplitter.rest.dto.PersonDto
import dk.lundogbendsen.demo.zplitter.rest.model.ExpenseModel
import dk.lundogbendsen.demo.zplitter.rest.model.PersonModel
import org.springframework.beans.factory.annotation.Autowired
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
    lateinit var eventRepo: EventRepository

    @Autowired
    lateinit var expenseRepo: ExpenseRepository

    @Autowired
    lateinit var personRepo: PersonRepository


    @Transactional
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody event: EventDto): ResponseEntity<EventModel> {
        if (event.id != null) {
            return ResponseEntity(HttpStatus.EXPECTATION_FAILED)
        }
        val saved = eventRepo.save(Event(null, event.name))
        return ResponseEntity(EventModel(saved), HttpStatus.CREATED)
    }

    @Transactional
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): ResponseEntity<List<EventModel>> {
        val list = eventRepo.findAll()
        return ResponseEntity(list.map { it ->
            getEventModel(it)
        }, HttpStatus.OK)
    }

    @Transactional
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<EventModel> {
        val event: Optional<Event?> = eventRepo.findById(id)
        if (event.isPresent) {
            return ResponseEntity(getEventModel(event.get()), HttpStatus.OK);
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Transactional
    @PostMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addExpense(@PathVariable("id") id: Long, @RequestBody expenseDto: ExpenseDto): ResponseEntity<ExpenseModel> {
        val eventOpt: Optional<Event?> = eventRepo.findById(id)

        if (!eventOpt.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val event = eventOpt.get()

        val personOpt = personRepo.findById(expenseDto.personId)
        if (!personOpt.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val person = personOpt.get()

        val expense = Expense(null, expenseDto.description, expenseDto.amount, person, event)
        event.expenses.add(expense)
        expenseRepo.save(expense)

        val expenseModel = ExpenseModel(expense)
        if (person.id != null) {
            val linkToPerson = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Persons::class.java).findById(person.id)).withRel("expense.person")
            expenseModel.add(linkToPerson)
        }
        if (event.id != null) {
            val linkToEvent = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Events::class.java).findById(event.id)).withRel("expense.event")
            expenseModel.add(linkToEvent)
        }
        return ResponseEntity(expenseModel, HttpStatus.CREATED);

    }


    @Transactional
    @PostMapping("/{id}/persons", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addPerson(@PathVariable("id") eventId: Long, @RequestBody personDto: PersonDto): ResponseEntity<PersonModel> {
        val eventOpt: Optional<Event?> = eventRepo.findById(eventId)

        if (!eventOpt.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val event = eventOpt.get()

        if (personDto.id == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }


        val personOpt = personRepo.findById(personDto.id)
        if (!personOpt.isPresent) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val person = personOpt.get()

        event.persons.add(person)
        person.events.add(event)

        val personModel = PersonModel(person)

        if (person.id != null) {
            val linkToPerson = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Persons::class.java).findById(person.id)).withRel("self")
            personModel.add(linkToPerson)
        }
        person.events.forEach {
            if (it.id!=null) {
                val linkToEvent = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Events::class.java).findById(it.id)).withRel("person.event")
                personModel.add(linkToEvent)
            }
        }
        return ResponseEntity(personModel, HttpStatus.CREATED);
    }


    @Transactional
    @DeleteMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteById(@PathVariable("id") id: Long): ResponseEntity<EventModel> {
        val event: Optional<Event?> = eventRepo.findById(id)
        if (event.isPresent) {
            val responseEntity = ResponseEntity(getEventModel(event.get()), HttpStatus.OK)
            eventRepo.delete(event.get())
            return responseEntity
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

        //todo change so that it conforms to eventModel.expenses.addAll(event.expenses.map...
        event.expenses.forEach { expense ->
            val em = ExpenseModel(expense)

            if (expense.person.id != null) {
                val linkToPerson = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Persons::class.java).findById(expense.person.id)).withRel("expense.person")
                em.add(linkToPerson)
            }
            eventModel.expenses.add(em)
        }
        return eventModel
    }
}