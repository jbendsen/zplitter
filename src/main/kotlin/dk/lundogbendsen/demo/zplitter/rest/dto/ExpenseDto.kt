package dk.lundogbendsen.demo.zplitter.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import javax.persistence.*


data class ExpenseDto (
        public val id: Long?,

        public val description: String,

        public val amount: Double,

        public val personId: Long
)
