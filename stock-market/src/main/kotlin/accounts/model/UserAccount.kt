package accounts.model

data class UserAccount(val name: String, val balance: Long, val stocks: Map<String, Long>)
