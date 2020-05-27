import kotlin.concurrent.timer
import kotlin.system.exitProcess

fun main(args:Array<String>){
    val argsMap:Map<String,String> = args.toList().windowed(2).map { (a,b) -> a to b }.toMap()
    val interval:Long
    try {
        interval = argsMap["-i"]?.toLong() ?: 10
    }catch(e:NumberFormatException){
        println("The interval value is not in the correct format. Please use a number format.")
        exitProcess(1)
    }

    timer(period=interval*1000){
        println("szekszekszek")
    }
}
