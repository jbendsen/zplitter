package dk.lundogbendsen.demo.zplitter.rest.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import dk.lundogbendsen.demo.zplitter.model.Person
import org.springframework.hateoas.RepresentationModel

@JsonIgnoreProperties(ignoreUnknown = true)
open class ExpenseModel(id:Long?, description:String, amount:Double) : RepresentationModel<ExpenseModel>() {
    var id = id
    var description = description
    var amount = amount

    constructor(expense : Expense) :
        this(expense.id, expense.description, expense.amount) {
    }
}