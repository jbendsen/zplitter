package dk.lundogbendsen.demo.zplitter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ZplitterApplication

fun main(args: Array<String>) {
	runApplication<ZplitterApplication>(*args)
}
