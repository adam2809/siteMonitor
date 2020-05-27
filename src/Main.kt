fun main(args:Array<String>) {
    val argsMap = args.toList().windowed(2).map { (a,b) -> a to b }
    println(argsMap)
}