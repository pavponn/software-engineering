package visitor

import token.BracketsToken
import token.NumberToken
import token.OperationToken
import token.Token

interface TokenVisitor {
    fun visit(token: NumberToken)

    fun visit(token: OperationToken)

    fun visit(token: BracketsToken)

    fun visitAll(tokens: List<Token>)


}