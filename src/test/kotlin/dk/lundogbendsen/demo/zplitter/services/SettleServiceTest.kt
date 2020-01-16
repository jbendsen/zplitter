package dk.lundogbendsen.demo.zplitter.services

import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Expense
import dk.lundogbendsen.demo.zplitter.model.Person
import dk.lundogbendsen.demo.zplitter.rest.services.SettleService
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SettleServiceTest {

    @Test
    fun testEmptyExpenseList() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val settleDebts = service.settleDebts(event)
        Assertions.assertEquals(0, settleDebts.size)
    }

    @Test
    fun testExpense() {

        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val settleDebts = service.settleDebts(event)
        Assertions.assertEquals(0, settleDebts.size)
    }


    @Test
    fun testExpense2() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val p1 = Person(id = 0, name = "A")
        val p2 = Person(id = 1, name = "B")
        val e1 = Expense(id = 1, amount = 100.0, description = "Food", person = p1, event = event)
        val e2 = Expense(id = 2, amount = 300.0, description = "Drinx", person = p1, event = event)
        val e3 = Expense(id = 3, amount = 200.0, description = "Burger", person = p2, event = event)
        val e4 = Expense(id = 4, amount = 170.0, description = "Beer", person = p2, event = event)
        event.expenses.add(e1)
        event.expenses.add(e2)
        event.expenses.add(e3)
        event.expenses.add(e4)
        event.persons.add(p1)
        event.persons.add(p2)
        val transferplan = service.settleDebts(event)
        assert(transferplan.size==1)

        print(transferplan)
        //Assertions.assertEquals(0, transferplan.size)

    }

    @Test
    fun testExpense3() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val p1 = Person(id = 0, name = "A")
        val p2 = Person(id = 1, name = "B")
        val p3 = Person(id = 2, name = "C")
        val e1 = Expense(id = 1, amount = 100.0, description = "Food", person = p1, event = event)
        val e2 = Expense(id = 2, amount = 200.0, description = "Drinx", person = p1, event = event)
        event.expenses.add(e1)
        event.expenses.add(e2)
        event.persons.add(p1)
        event.persons.add(p2)
        event.persons.add(p3)
        val transferplan = service.settleDebts(event)
        print(transferplan)
        Assertions.assertEquals(2, transferplan.size)
        transferplan.forEach{Assertions.assertEquals(100.0, it.amount)}
    }

    @Test
    fun testExpense4() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val p1 = Person(id = 0, name = "A")
        val p2 = Person(id = 1, name = "B")
        val p3 = Person(id = 2, name = "C")
        val e1 = Expense(id = 1, amount = 100.0, description = "Food", person = p2, event = event)
        val e2 = Expense(id = 2, amount = 200.0, description = "Drinx", person = p1, event = event)
        event.expenses.add(e1)
        event.expenses.add(e2)
        event.persons.add(p1)
        event.persons.add(p2)
        event.persons.add(p3)
        val transferplan = service.settleDebts(event)
        print(transferplan)
        Assertions.assertEquals(1, transferplan.size)
        transferplan.forEach{Assertions.assertEquals(100.0, it.amount)}
    }

    @Test
    fun testExpense5() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val p1 = Person(id = 0, name = "A")
        val p2 = Person(id = 1, name = "B")
        val p3 = Person(id = 2, name = "C")
        val p4 = Person(id = 3, name = "D")
        val p5 = Person(id = 4, name = "E")
        val e1 = Expense(id = 1, amount = 100.0, description = "Food", person = p1, event = event)
        val e2 = Expense(id = 2, amount = 200.0, description = "Drinx", person = p1, event = event)
        val e3 = Expense(id = 3, amount = 150.0, description = "Food", person = p2, event = event)
        val e4 = Expense(id = 4, amount = 400.0, description = "Drinx", person = p3, event = event)
        val e5 = Expense(id = 5, amount = 500.0, description = "Food", person = p4, event = event)
        val e6 = Expense(id = 6, amount = 60.0, description = "Drinx", person = p5, event = event)
        val e7 = Expense(id = 7, amount = 123.0, description = "Food", person = p5, event = event)
        val e8 = Expense(id = 8, amount = 901.0, description = "Drinx", person = p5, event = event)

        event.expenses.add(e1)
        event.expenses.add(e2)
        event.expenses.add(e3)
        event.expenses.add(e4)
        event.expenses.add(e5)
        event.expenses.add(e6)
        event.expenses.add(e7)
        event.expenses.add(e8)

        event.persons.add(p1)
        event.persons.add(p2)
        event.persons.add(p3)
        event.persons.add(p4)
        event.persons.add(p5)
        val transferplan = service.settleDebts(event)
        println(transferplan)
        println(transferplan.size)

        //Assertions.assertEquals(1, transferplan.size)
        //transferplan.forEach{Assertions.assertEquals(100.0, it.amount)}
    }

    @Test
    fun testExpense6() {
        val service = SettleService()
        val event = Event(0, "Roskilde") //event has an empty expense list as default
        val p1 = Person(id = 0, name = "A")
        val p2 = Person(id = 1, name = "B")
        val p3 = Person(id = 2, name = "C")
        val p4 = Person(id = 3, name = "D")
        val p5 = Person(id = 4, name = "E")
        val e1 = Expense(id = 1, amount = 1000.0, description = "Food", person = p1, event = event)

        event.expenses.add(e1)

        event.persons.add(p1)
        event.persons.add(p2)
        event.persons.add(p3)
        event.persons.add(p4)
        event.persons.add(p5)
        val transferplan = service.settleDebts(event)
        println(transferplan)
        println(transferplan.size)

        Assertions.assertEquals(4, transferplan.size)
        transferplan.forEach{Assertions.assertEquals(200.0, it.amount)}
    }



}

