package dk.lundogbendsen.demo.zplitter.rest.services.Transfer

import dk.lundogbendsen.demo.zplitter.model.Person

data class Transfer (
        val from:Person,
        val to:Person,
        val amount:Double
)
