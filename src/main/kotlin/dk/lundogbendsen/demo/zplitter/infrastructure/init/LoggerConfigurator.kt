package dk.lundogbendsen.demo.zplitter.infrastructure.init

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class LoggerConfigurator {
    @Bean
    @Scope("prototype")
    fun produceLogger(injectionPoint: InjectionPoint): Logger {
        val classOnWired = injectionPoint.member.declaringClass
        return LoggerFactory.getLogger(classOnWired)
    }
}