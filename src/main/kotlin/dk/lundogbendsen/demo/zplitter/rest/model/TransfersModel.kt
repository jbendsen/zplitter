package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
open class TransfersModel(name:String, event: Event) : RepresentationModel<TransfersModel>() {
    val event = EventModel(event)
    val name = name
    val date = LocalDate.now()
    val transfers = mutableListOf<TransferModel>()

}