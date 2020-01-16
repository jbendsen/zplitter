package dk.lundogbendsen.demo.zplitter.rest.resources

import dk.lundogbendsen.demo.zplitter.dao.ExpenseRepository
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import dk.lundogbendsen.demo.zplitter.rest.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("api/expenses")
class Expenses {

    @Autowired
    lateinit var expenseRepo: ExpenseRepository

    @Transactional
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findById(@PathVariable("id") id: Long): ResponseEntity<ExpenseModel> {
        val expenseOpt: Optional<Expense?> = expenseRepo.findById(id)
        if (expenseOpt.isPresent) {
            return ResponseEntity(getExpenseModel(expenseOpt.get()), HttpStatus.OK);
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }


    private fun getExpenseModel(expense : Expense): ExpenseModel {
        val expenseModel = ExpenseModel(expense.id, expense.description, expense.amount)
        if (expense.id != null) {
            val linkToSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(Expenses::class.java).findById(expense.id)).withSelfRel()
            expenseModel.add(linkToSelf)
        }
        return expenseModel
    }
}