package dk.lundogbendsen.demo.zplitter.model

import javax.persistence.*

@Entity
class Person (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public val id: Long? = null,
    @Column(nullable = false)
    public val name: String,
    @ManyToMany
    public val events : List<Event> = emptyList()
)

