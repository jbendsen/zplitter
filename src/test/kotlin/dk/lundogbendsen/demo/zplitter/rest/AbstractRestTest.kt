package dk.lundogbendsen.demo.zplitter.rest

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import dk.lundogbendsen.demo.zplitter.ZplitterApplication
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.IOException


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ZplitterApplication::class])
@WebAppConfiguration
abstract class AbstractRestTest {

    @Autowired
    protected var mvc: MockMvc? = null


    @Autowired
    var webApplicationContext: WebApplicationContext? = null

    protected fun setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    @Throws(JsonProcessingException::class)
    protected fun mapToJson(obj: Any?): String {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(obj)
    }

    @Throws(JsonParseException::class, JsonMappingException::class, IOException::class)
    protected fun <T> mapFromJson(json: String?, clazz: Class<T>?): T {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(json, clazz)
    }

    public fun asJsonString(obj : Any) : String {
        try {
            val mapper = ObjectMapper()
            val jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (e: Exception) {
            throw RuntimeException(e);
        }
    }

}