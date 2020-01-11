package dk.lundogbendsen.demo.zplitter.model

import javax.persistence.*

@Entity
class Person (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public val id: Long? = null,
    @Column(nullable = false)
    public val name: String,
    @ManyToMany(cascade = [CascadeType.PERSIST,CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH])
    public val events : MutableList<Event> = mutableListOf()
)

