package visitor

import token.*
import java.lang.IllegalArgumentException
import java.util.ArrayDeque

class CalcVisitor : TokenVisitor {
    private val stack = ArrayDeque<Int>()
    var result: Int? = null
        private set

    override fun visit(token: NumberToken) {
        stack.push(token.getValue())
    }

    override fun visit(token: OperationToken) {
        assert(stack.size > 1) { "Can't calculate given expression" }
        val x = stack.pop()
        val y = stack.pop()
        val func = getOperation(token.getTokenType())
            ?: throw IllegalArgumentException("Operation ${token.getTokenType()} is not supported")
        stack.push(func(y, x))
    }

    override fun visit(token: BracketsToken) {
        throw IllegalArgumentException("Brackets can't be used in expression (reverse polish notation)")
    }

    override fun visitAll(tokens: List<Token>) {
        tokens.forEach { it.accept(this) }
        assert(stack.size == 1) { "Can't calculate given expression" }
        result = stack.pop()
    }


    private fun getOperation(tokenType: TokenType): ((Int, Int) -> Int)? {
        return when (tokenType) {
            TokenType.PLUS -> Int::plus
            TokenType.MUL -> Int::times
            TokenType.DIV -> Int::div
            TokenType.MINUS -> Int::minus
            else -> null
        }
    }


}