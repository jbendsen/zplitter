package dk.lundogbendsen.demo.zplitter.model

import javax.persistence.*

@Entity
data class Event (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public val id: Long?,

    @Column(nullable = false)
    public val name: String,

    @ManyToMany(cascade = [CascadeType.ALL], mappedBy = "events")
    public val persons: MutableList<Person> = mutableListOf()

)
