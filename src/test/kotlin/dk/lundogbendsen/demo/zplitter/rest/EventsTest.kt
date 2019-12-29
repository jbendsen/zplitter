package dk.lundogbendsen.demo.zplitter.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dk.lundogbendsen.demo.zplitter.rest.model.EventModel
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


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
    fun getProductById() {
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
}

