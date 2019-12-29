package dk.lundogbendsen.demo.zplitter.rest.resources

import dk.lundogbendsen.demo.zplitter.dao.PersonRepository
import dk.lundogbendsen.demo.zplitter.model.Person
import dk.lundogbendsen.demo.zplitter.rest.model.PersonModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/persons")
open class Persons {

    @Autowired
    lateinit var repo: PersonRepository

    @Transactional
    @GetMapping("/{id}", produces=[MediaType.APPLICATION_JSON_VALUE])
    open fun findById(@PathVariable("id") id:Long): ResponseEntity<PersonModel> {
        val person = repo.findById(id).get()

        if (person==null) {
            return ResponseEntity<PersonModel>(HttpStatus.NOT_FOUND)
        } else {
            val personModel = PersonModel(person.id, person.name)
            if (person.id != null) {
                val linkToSelf = linkTo(methodOn(Persons::class.java).findById(person.id)).withSelfRel()
                personModel.add(linkToSelf)
            }
            return ResponseEntity<PersonModel>(personModel,HttpStatus.FOUND)
        }
    }

        @Transactional
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun list(): List<PersonModel> {
        repo.save(Person(name="Kiefer ${System.currentTimeMillis()}"))
        val list = repo.findAll()
        return list.map { it -> getPersonModel(it?.id ?: 0, it?.name ?: "n/a") }
    }

    private fun getPersonModel(id:Long, name:String) : PersonModel {
        val personModel = PersonModel(id, name)
        val linkToSelf = linkTo(methodOn(Persons::class.java).findById(id)).withSelfRel()
        personModel.add(linkToSelf)
        return personModel
    }
}



