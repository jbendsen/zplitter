package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import org.springframework.hateoas.RepresentationModel

@JsonIgnoreProperties(ignoreUnknown = true)
open class EventModel(id:Long?, name:String) : RepresentationModel<EventModel>() {
        var id:Long? = id
        var name = name
        val persons : MutableList<PersonModel> = mutableListOf()
        val expenses : MutableList<ExpenseModel> = mutableListOf()
        constructor(event : Event) : this(event.id, event.name) {
        }

        constructor() : this(null, "") {
        }
}
