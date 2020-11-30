package token

class Tokenizer {
    private val tokens = ArrayList<Token>()
    private var curState: State = StartState()

    fun tokenize(s: String): List<Token> {
        curState = StartState()
        s.forEach { curState.handle(it) }
        curState.handleEOF()
        require(curState is EOFState)
        return tokens
    }

    private abstract inner class State {
        abstract fun handle(c: Char)
        abstract fun handleEOF()
    }

    private inner class EOFState : State() {
        override fun handle(c: Char) {
            throw UnsupportedOperationException("No characters could be handled in EOF state")
        }

        override fun handleEOF() {}
    }

    private inner class DigitState : State() {
        private var number = 0

        override fun handle(c: Char) {
            when (c) {
                in '0'..'9' -> {
                    number = number * 10 + (c - '0')
                }
                else -> {
                    this@Tokenizer.tokens.add(NumberToken(number))
                    this@Tokenizer.curState = StartState()
                    this@Tokenizer.curState.handle(c)
                }
            }
        }

        override fun handleEOF() {
            this@Tokenizer.tokens.add(NumberToken(number))
            this@Tokenizer.curState = EOFState()
        }
    }

    private inner class StartState : State() {
        override fun handle(c: Char) {
            when (c) {
                '(' -> this@Tokenizer.tokens.add(BracketsToken(TokenType.LEFT))
                ')' -> this@Tokenizer.tokens.add(BracketsToken(TokenType.RIGHT))
                '+' -> this@Tokenizer.tokens.add(OperationToken(TokenType.PLUS))
                '-' -> this@Tokenizer.tokens.add(OperationToken(TokenType.MINUS))
                '*' -> this@Tokenizer.tokens.add(OperationToken(TokenType.MUL))
                '/' -> this@Tokenizer.tokens.add(OperationToken(TokenType.DIV))
                in '0'..'9' -> {
                    this@Tokenizer.curState = DigitState()
                    this@Tokenizer.curState.handle(c)
                }
                else -> {
                    require(c.isWhitespace()) { "Unexpected character '$c' in the input" }
                }
            }
        }

        override fun handleEOF() {
            this@Tokenizer.curState = EOFState()
        }
    }
}