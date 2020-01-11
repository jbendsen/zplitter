package dk.lundogbendsen.demo.zplitter.rest.dto

import javax.persistence.*

@Entity
data class EventDto (
    public val id: Long?,
    public val name: String
)
