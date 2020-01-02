package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.hateoas.RepresentationModel

@JsonIgnoreProperties(ignoreUnknown = true)
open class PersonModel(id:Long?, name:String) : RepresentationModel<PersonModel>() {
    var id:Long? = id
    var name = name
    constructor(person : Person) : this(person.id, person.name) {
    }

    constructor() : this(null, "") {
    }
}