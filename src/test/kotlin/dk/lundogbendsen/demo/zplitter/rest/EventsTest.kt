package dk.lundogbendsen.demo.zplitter.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dk.lundogbendsen.demo.zplitter.rest.dto.PersonDto
import dk.lundogbendsen.demo.zplitter.rest.model.EventModel
import dk.lundogbendsen.demo.zplitter.rest.model.PersonModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.ArrayList


class EventsTest : AbstractRestTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
    }

    @Test
    @Sql("/integration_test_cleanup.sql")
    fun createEventTest() {
        val event = EventModel(null, "Tinderbox")

        val result = mvc!!.perform(post("/api/events")
                .content(asJsonString(event))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()

        val s = result.response.contentAsString
        val resultEvent = ObjectMapper().readValue(s, EventModel::class.java)

        assertTrue(resultEvent.id != null)
        assertTrue(resultEvent.name == "Tinderbox")
    }


    @Test()
    @Sql("/integration_test_cleanup.sql")
    fun addPersonsToEventTest() {
        val event = createEvent()
        val persons = listOf<PersonModel>(createPerson("Anne"), createPerson("Burt"), createPerson("Chloe"))

        persons.map{it -> PersonDto(it.id, it.name)}.forEach{
            mvc!!.perform(post("/api/events/${event.id}/persons")
            .content(asJsonString(it))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        }

        val result = mvc!!.perform(get("/api/events/${event.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()

        val event2  = ObjectMapper().readValue<EventModel>(result.response.contentAsString)
        assertEquals(3, event2.persons.size)
        persons.forEach{ assertTrue(event2.persons.contains(it))}
    }

    @Test
    @Sql("/integration_test_cleanup.sql")
    fun listEventsTest() {
        //create new event
        val createEvent = createEvent()
        val eventName = createEvent.name

        //get all events
        val result = mvc!!.perform(get("/api/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        val list  = ObjectMapper().readValue<List<EventModel>>(s)
        assertTrue(list.size > 0)

        //is new event among events?
        val filtered = list.filter { it -> it.name == eventName}
        assertTrue(filtered.size>0);
    }

    @Test
    @Sql("/integration_test_cleanup.sql")
    fun getEventById() {
        val createEvent = createEvent()

        //get all events
        val result = mvc!!.perform(get("/api/events/${createEvent.id}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        val event  = ObjectMapper().readValue<EventModel>(s)
        assertTrue(event.name == createEvent.name);
        assertTrue(event.id == createEvent.id);
    }

    @Test
    @Sql("/integration_test_cleanup.sql")
    fun deleteEventTest() {
        //create a new event
        val event = EventModel(null, "Tinderbox")
        val result = mvc!!.perform(post("/api/events")
                .content(asJsonString(event))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        val resultEvent = ObjectMapper().readValue(s, EventModel::class.java)

        //get all events and save number of events
        val result2 = mvc!!.perform(get("/api/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s2 = result2.response.contentAsString
        val resultEvent2 = ObjectMapper().readValue(s2, ArrayList::class.java)
        val startSize = resultEvent2.size

        //delete the just greated event
        mvc!!.perform(delete("/api/events/${resultEvent.id}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()

        //get all events and assure that there is one less
        val result4 = mvc!!.perform(get("/api/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s4 = result4.response.contentAsString
        val resultEvent4 = ObjectMapper().readValue(s4, ArrayList::class.java)
        val endSize = resultEvent4.size
        assertTrue(startSize-1==endSize)
    }


    private fun createEvent() : EventModel {
        val eventName = "Tinderbox" + System.currentTimeMillis()
        val event = EventModel(null, eventName)

        val result = mvc!!.perform(post("/api/events")
                .content(asJsonString(event))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        return ObjectMapper().readValue(s, EventModel::class.java)
    }

    private fun createPerson(name:String) : PersonModel {
        val person = PersonDto(null, name)

        val result = mvc!!.perform(post("/api/persons")
                .content(asJsonString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        return ObjectMapper().readValue(s, PersonModel::class.java)
    }
}

