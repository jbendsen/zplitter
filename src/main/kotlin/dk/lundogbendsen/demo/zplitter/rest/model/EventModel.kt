package dk.lundogbendsen.demo.zplitter.rest.model

import dk.lundogbendsen.demo.zplitter.model.Event
import org.springframework.hateoas.RepresentationModel

open class EventModel(id:Long?, name:String) : RepresentationModel<EventModel>() {
        var id:Long? = id
        var name = name
        val persons : MutableList<PersonModel> = mutableListOf()

        constructor(event : Event) : this(event.id, event.name) {
        }

        constructor() : this(null, "") {
        }
}
