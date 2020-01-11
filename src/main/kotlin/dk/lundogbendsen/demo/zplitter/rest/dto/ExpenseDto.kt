package dk.lundogbendsen.demo.zplitter.rest.dto;

data class ExpenseDto (
        public val id: Long?,

        public val description: String,

        public val amount: Double,

        public val personId: Long
)
