package dk.lundogbendsen.demo.zplitter.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dk.lundogbendsen.demo.zplitter.model.Person
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


class PersonsTest : AbstractRestTest() {

    @BeforeEach
    override fun setUp() {
        super.setUp()
    }


    @Test
    @Sql("/integration_test_cleanup.sql")
    fun createPersonTest() {
        val event = Person(null, "Anne")

        val person = createPerson("Anne");
        assertTrue(person.id != null)
        assertTrue(person.name == "Anne")
    }



    @Test
    @Sql("/integration_test_cleanup.sql")
    fun listPersonsTest() {
        //create new event
        createPerson("Burt");
        createPerson("Chloe");

        //get all persons
        val result = mvc!!.perform(get("/api/persons")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        val s = result.response.contentAsString
        val list  = ObjectMapper().readValue<List<PersonModel>>(s)
        assertEquals(2, list.size)
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

