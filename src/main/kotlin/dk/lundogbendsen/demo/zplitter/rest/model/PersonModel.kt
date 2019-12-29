package dk.lundogbendsen.demo.zplitter.rest.model

import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.hateoas.RepresentationModel

open class PersonModel(id:Long?, name:String) : RepresentationModel<PersonModel>() {
    var id:Long? = id
    var name = name
    constructor(person : Person) : this(person.id, person.name) {
    }

    constructor() : this(null, "") {
    }
}