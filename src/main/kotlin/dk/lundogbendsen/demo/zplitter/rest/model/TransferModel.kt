package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.hateoas.RepresentationModel

@JsonIgnoreProperties(ignoreUnknown = true)
open class TransferModel(from:Person, to:Person, amount:Double) : RepresentationModel<TransferModel>() {
    val from = PersonModel(from)
    val to = PersonModel(to)
    val amount = amount
}