package dk.lundogbendsen.demo.zplitter.infrastructure

import dk.lundogbendsen.demo.zplitter.dao.EventRepository
import dk.lundogbendsen.demo.zplitter.model.Event
import dk.lundogbendsen.demo.zplitter.model.Person
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component


@Component
class DatabaseInitializer : ApplicationListener<ContextRefreshedEvent?> {

    @Autowired
    lateinit var log : Logger

    @Autowired
    lateinit var repo : EventRepository

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("Start creating data...")
        val rf = Event(null, "Roskildefestival 2020")
        val peter = Person(null, "Peter")
        rf.persons.add(peter)
        peter.events.add(rf)

        val lone = Person(null, "Lone")
        rf.persons.add(lone)
        lone.events.add(rf)

        val karen = Person(null, "Karen")
        rf.persons.add(karen)
        karen.events.add(rf)

        repo.save(rf)

        log.info("End creating data...")
    }
}

