import token.Tokenizer
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.PrintVisitor

fun main() {
    val input = readLine()

    if (input == null) {
        println("No input provided")
        return
    }

    try {
        val printVisitor = PrintVisitor()
        val tokens = Tokenizer().tokenize(input)
        println("Initial expression")
        printVisitor.visitAll(tokens)
        val parseVisitor = ParserVisitor()
        parseVisitor.visitAll(tokens)
        val reversePolishNotation = parseVisitor.result
        println("Transformed to reverse polish notation")
        printVisitor.visitAll(reversePolishNotation)
        val calcVisitor = CalcVisitor()
        calcVisitor.visitAll(reversePolishNotation)
        println("Calculated result: ${calcVisitor.result}")
    } catch (t: Throwable) {
        println("Error occurred, more info: ${t.message}")
    }
}