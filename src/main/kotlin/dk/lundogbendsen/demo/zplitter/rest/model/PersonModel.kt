package dk.lundogbendsen.demo.zplitter.rest.model

import org.springframework.hateoas.RepresentationModel

class PersonModel(id:Long?, name:String) : RepresentationModel<PersonModel>() {
    var id:Long? = id
    var name = name
}