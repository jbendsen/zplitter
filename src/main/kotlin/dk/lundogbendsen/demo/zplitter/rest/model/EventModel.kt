package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonProperty
import dk.lundogbendsen.demo.zplitter.model.Event
import org.springframework.hateoas.RepresentationModel


data class EventModel  (
        @JsonProperty("id")
        public val id: Long?,
        @JsonProperty("name")
        public val name: String,

        @JsonProperty("persons")
        public val persons : List<PersonModel> = emptyList()) : RepresentationModel<EventModel>()
{
        constructor(event: Event) : this(event.id, event.name)
}
