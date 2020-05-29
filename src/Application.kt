import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

class Application(args: Array<String>) {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private val propsFileURL: URL = Application::class.java.classLoader.getResource("app.properties")
    private val props  = FileInputStream(File(propsFileURL.toURI())).use {
        Properties().apply { load(it) }
    }

    init {
        val argsMap:Map<String,String> = args.toList().windowed(2).map { (a,b) -> a to b }.toMap()

        argsMap["-a"]?.let(::addNewWebsiteToTracking)
        argsMap["-i"]?.let(::changeIntervalProperty)

        props.store(FileOutputStream(File(propsFileURL.toURI())),null)

        val interval = props.getProperty("interval").toLong()
        val websitesToTrack = props.getProperty("trackedWebsites").split(",").map(::URL)

        Timer().scheduleAtFixedRate(TrackerTimerTask(websitesToTrack,interval.toInt()),0,interval*1000)
    }

    private fun addNewWebsiteToTracking(urlStr:String){
//        TODO don't add websites that are already there
        try {
            URL(urlStr.let(::test))
        }catch (e:MalformedURLException){
            println("The URL of the website you wanted to add is invalid.")
        }

        val currPropVal = props.getProperty("trackedWebsites")
        val newPropVal = if(currPropVal == null){
            urlStr
        }else{
            "${currPropVal},${urlStr}"
        }

        props.setProperty("trackedWebsites",newPropVal)
    }

    private fun changeIntervalProperty(newValStr:String){
        try {
            newValStr.toInt()
        }catch(e:NumberFormatException){
            println("The interval value is not in the correct format. Please use a number format.")
            exitProcess(1)
        }

        props.setProperty("interval",newValStr)
    }

    private fun test(s:String):String{
        if(!(s.startsWith("https://") || s.startsWith("http://"))){
            return "https://$s"
        }
        return s
    }
}