package dk.lundogbendsen.demo.zplitter.dao


import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import java.util.*


interface ExpenseRepository : JpaRepository<Expense?, Long?> {
    fun findByDescription(description: String?): List<Expense?>?
}