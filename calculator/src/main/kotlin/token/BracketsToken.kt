package token

import visitor.TokenVisitor

class BracketsToken(private val bracketType: TokenType): Token {
    init {
        require(
            bracketType == TokenType.LEFT ||
                    bracketType == TokenType.RIGHT
        )
    }
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }

    override fun getTokenType(): TokenType {
       return bracketType
    }

    override fun toString(): String {
        return bracketType.toString()
    }
}