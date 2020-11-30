package token

import visitor.TokenVisitor

class NumberToken(private val value: Int) : Token {

    fun getValue() = this.value

    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }

    override fun getTokenType(): TokenType {
        return TokenType.NUMBER
    }

    override fun toString(): String {
        return "${TokenType.NUMBER}($value)"
    }

}