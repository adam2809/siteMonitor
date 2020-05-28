import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.concurrent.timer
import kotlin.system.exitProcess

class Application(args: Array<String>) {

    init {
        val argsMap:Map<String,String> = args.toList().windowed(2).map { (a,b) -> a to b }.toMap()

        val interval:Long
        try {
            interval = argsMap["-i"]?.toLong() ?: 10
        }catch(e:NumberFormatException){
            println("The interval value is not in the correct format. Please use a number format.")
            exitProcess(1)
        }

        argsMap["-a"]?.let(::addNewWebsiteToTracking)

//        timer(period=interval*1000){
//
//        }
    }

    private fun addNewWebsiteToTracking(urlStr:String){
        try {
            URL(urlStr.let(::test))
        }catch (e:MalformedURLException){
            println("The URL of the website you wanted to add is invalid.")
        }


        val propsFileURL = Application::class.java.classLoader.getResource("app.properties")
        val props  = FileInputStream(File(propsFileURL.toURI())).use {
            Properties().apply { load(it) }
        }

        val newPropVal = "${props.getProperty("trackedWebsites")},${urlStr}"
        props.setProperty("trackedWebsites",newPropVal)

        props.store(FileOutputStream(File(propsFileURL.toURI())),null)
    }

    private fun test(s:String):String{
        if(!(s.startsWith("https://") || s.startsWith("http://"))){
            return "https://$s"
        }
        return s
    }
}