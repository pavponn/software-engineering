package visitor

import token.*
import token.TokenType.*
import java.util.ArrayDeque

class ParserVisitor : TokenVisitor {
    private var stack = ArrayDeque<Token>()
    var result = mutableListOf<Token>()
        private set

    private fun priority(op: OperationToken): Int {
        return when (op.getTokenType()) {
            MUL, DIV -> 1
            else -> 0

        }
    }

    override fun visit(token: NumberToken) {
        result.add(token)
    }

    override fun visit(token: OperationToken) {
        while (!stack.isEmpty()) {
            val lastToken = stack.peek()
            if (lastToken is OperationToken) {
                val lastTokenPriority = priority(lastToken)
                val tokenPriority = priority(token)
                if (tokenPriority <= lastTokenPriority) {
                    stack.pop()
                    result.add(lastToken)
                } else {
                    break
                }
            } else {
                break
            }
        }
        stack.push(token)
    }

    override fun visit(token: BracketsToken) {
        when (token.getTokenType()) {
            LEFT -> stack.push(token)
            RIGHT -> {
                loop@ while (!stack.isEmpty()) {
                    val lastToken = stack.peek()
                    when (lastToken.getTokenType()) {
                        LEFT -> {
                            stack.pop()
                            break@loop
                        }
                        PLUS, MINUS, MUL, DIV -> {
                            stack.pop()
                            result.add(lastToken)
                        }
                        else -> {
                            throw IllegalArgumentException("Can't process given expression mda")
                        }
                    }
                }
            }
            else -> throw IllegalArgumentException("Type of token is invalid") // should not be thrown
        }
    }

    override fun visitAll(tokens: List<Token>) {
        result = mutableListOf()
        stack = ArrayDeque()
        tokens.forEach { it.accept(this) }
        while (!stack.isEmpty()) {
            val last = stack.peek()
            if (last is OperationToken) {
                result.add(last)
                stack.pop()
            } else {
                throw IllegalArgumentException("Can't process given expression: ${stack.toList()}")
            }

        }
    }

}