package dk.lundogbendsen.demo.zplitter.model

import javax.persistence.*


@Entity
data class Expense (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public val id: Long?,

        @Column(nullable = false)
        public val description: String,

        @Column(nullable = false)
        public var amount: Double,

        @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH])
        public val person: Person,

        @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH])
        public val event: Event
) {
        public fun amount(amt:Double) {
                if (amt<0) {
                        throw IllegalArgumentException("Expense cannot be negative")
                }
                amount = amt
        }
}