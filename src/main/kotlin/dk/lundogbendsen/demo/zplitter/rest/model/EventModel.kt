package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonProperty
import dk.lundogbendsen.demo.zplitter.model.Event
import org.springframework.hateoas.RepresentationModel


class EventModel  (id:Long?, name:String) : RepresentationModel<EventModel>() {
        var id:Long? = id
        var name = name
        public val persons : MutableList<PersonModel> = mutableListOf())
}
