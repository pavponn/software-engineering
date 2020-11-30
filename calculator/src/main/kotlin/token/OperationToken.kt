package token

import visitor.TokenVisitor

class OperationToken(private val operationType: TokenType) : Token {

    init {
        require(
            operationType == TokenType.PLUS ||
            operationType == TokenType.MINUS ||
            operationType == TokenType.DIV ||
            operationType == TokenType.MUL
        )
    }
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }

    override fun getTokenType(): TokenType {
        return operationType
    }

    override fun toString(): String {
        return operationType.toString()
    }

}