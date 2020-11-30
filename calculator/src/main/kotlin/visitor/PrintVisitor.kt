package visitor

import token.BracketsToken
import token.NumberToken
import token.OperationToken
import token.Token

class PrintVisitor : TokenVisitor {
    override fun visit(token: NumberToken) {
        print(token.toString())
    }

    override fun visit(token: OperationToken) {
        print(token.toString())
    }

    override fun visit(token: BracketsToken) {
        print(token.toString())
    }

    override fun visitAll(tokens: List<Token>) {
        for (i in tokens.indices) {
            tokens[i].accept(this)
            if (i != tokens.size - 1) {
                print(" ")
            }
        }
        println()
    }

}