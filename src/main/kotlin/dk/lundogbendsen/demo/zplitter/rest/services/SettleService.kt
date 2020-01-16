package dk.lundogbendsen.demo.zplitter.rest.services

import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import dk.lundogbendsen.demo.zplitter.model.Person
import dk.lundogbendsen.demo.zplitter.rest.services.Transfer.Transfer
import org.springframework.stereotype.Service
import kotlin.collections.mutableMapOf

@Service
class SettleService {

    fun settleDebts(event: Event): List<Transfer> {

        val expenses = event.expenses.deepCopy() //clone expenses. we don't want to mutate the persisted expenses
        val persons = event.persons
        //make sure all persons on the event are in the expense list by creating fake expenses of amount 0.0
        event.persons.forEach { p ->
            expenses.add(Expense(0, "n/a", 0.0, p, event))
        }
        val transfers = mutableListOf<Transfer>()
        val avg = calculateAverageExpense(event)
        println(avg)

        //map[person -> sum(expense)]
        val personWithExpenseSumIm = expenses.groupBy(Expense::person).mapValues { it.value.sumByDouble { it.amount } }

        val personWithExpenseSum = mutableMapOf<Person, Double>()
        val map = personWithExpenseSumIm.forEach { me ->
            personWithExpenseSum.put(me.key, me.value - avg)
        }

        println(personWithExpenseSum)

        //as long as there's unsettled expenses
        while (!isSettled(personWithExpenseSum)) {
            personWithExpenseSum.forEach {
                val current = it
                //current is owed money
                println("one again: " + personWithExpenseSum)
                if (current.value > 0) { //try to settle this expense
                    personWithExpenseSum.forEach {
                        if (it != current) { //skip it self
                            if (it.value < 0) { //it owes money
                                println("1: settle positive")
                                //it owes more or same amount as current
                                if (current.value + it.value <= 0.0) {
                                    println("1a: making transfer")
                                    transfers.add(Transfer(it.key, current.key, current.value))
                                    personWithExpenseSum.put(it.key, it.value + current.value)
                                    personWithExpenseSum.put(current.key, 0.0)
                                }
                            }
                        }
                    }
                }
                //current owes money
                if (current.value < 0) { //try to settle this expense
                    println("2: settle negative")
                    personWithExpenseSum.forEach {
                        if (it != current) { //skip it self
                            if (it.value > 0) { //it is owed money
                                println("2a: found positive")
                                //if current owes more money or same than it is owed
                                if (current.value + it.value <= 0) {
                                    println("2b: transfer to settle all +")
                                    transfers.add(Transfer(current.key, it.key, it.value))
                                    personWithExpenseSum.put(current.key, current.value + it.value)
                                    personWithExpenseSum.put(it.key, 0.0)
                                } else { //if current owes less money than it is owed
                                    println("2c: transfer to setlle some of +")
                                    transfers.add(Transfer(current.key, it.key, -current.value))
                                    personWithExpenseSum.put(it.key, current.value + it.value)
                                    personWithExpenseSum.put(current.key, 0.0)

                                }
                            }
                        }
                    }
                }
            }
        }
        return transfers;
    }


    //extension method: makes a deep copy of any List<Expenses>
    fun List<Expense>.deepCopy(): MutableList<Expense> {
        val list = mutableListOf<Expense>()
        this.forEach { list.add(it.copy()) }
        return list
    }

    //is there any debt left
    fun isSettled(expenses: Map<Person, Double>): Boolean {
        if (expenses.size == null) {
            return true
        }
        return expenses.filter { !it.value.virtuallyZero() }.size == 0
    }

    //extension method: adds virtuallyZero method to Double class
    fun Double.virtuallyZero(): Boolean {
        return Math.abs(this) < 0.0001
    }


    fun calculateAverageExpense(event: Event): Double {
        val numberOfPersons = event.persons.size
        val sum = event.expenses.sumByDouble { it.amount }
        return sum / numberOfPersons;
    }


}