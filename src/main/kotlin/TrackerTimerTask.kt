import availabilitycheckers.URLAvailabilityChecker
import java.util.*
import java.net.URL as URL

class TrackerTimerTask(websitesToTrack:List<URL>, private val interval:Int, private val checker: URLAvailabilityChecker): TimerTask() {

    private var last1MinutePrint = System.currentTimeMillis()
    private var last10MinutePrint = System.currentTimeMillis()

    private val currRaisedAlerts = mutableSetOf<URL>()

    private val dataPoints:Map<URL,MutableList<TrackerDataPoint>> = websitesToTrack.map { it to mutableListOf<TrackerDataPoint>() }.toMap()
    private val maxDataPoints = 3600/interval

    override fun run() {
        updateDataPoints()

        if(dataPoints.values.first().size >= 120 / interval){
            alertIfNeeded()
            alertRecoveryIfNeeded()
        }

        printInfoIfNeeded()
    }

    private fun updateDataPoints(){
        dataPoints.entries.forEach { (url,data) ->
            if(data.size > maxDataPoints){
                data.drop(1)
            }

            val requestStartTime = System.currentTimeMillis()
            val responseCode:Int? = checker.checkStatusCode(url)

            data.add(TrackerDataPoint(responseCode,System.currentTimeMillis()-requestStartTime))
        }
    }

    private fun alertIfNeeded(){
        dataPoints.entries.forEach { (url,data) ->
            if(url in currRaisedAlerts){
                return
            }
            val relevantData = data.takeLast(120/interval)
            val availability = getAvailability(relevantData)
            if (availability<80){
                currRaisedAlerts.add(url)
                println("Website $url is down. availability=$availability, time=${Date()}")
            }
        }
    }

    private fun alertRecoveryIfNeeded(){
        dataPoints.entries.forEach { (url,data) ->
            if(url !in currRaisedAlerts){
                return
            }
            val relevantData = data.takeLast(120/interval)
            val availability = getAvailability(relevantData)
            if(availability >= 80){
                currRaisedAlerts.remove(url)
                println("Website $url is back up. availability=$availability, time=${Date()}")
            }
        }
    }

    private fun printInfoIfNeeded() {
        val currTime = System.currentTimeMillis()
        when{
            currTime - last1MinutePrint > 60000 -> println("Printing stats of all websites for the past 10 minutes (${Date()})\n" +
                    getInfo(600 / interval)).also {
                last1MinutePrint=currTime
            }

            currTime - last10MinutePrint > 600000 -> println("Printing stats of all websites for the past 1 hour (${Date()})\n" +
                    getInfo(3600 / interval)).also {
                last10MinutePrint=currTime
            }
        }
    }

    private fun getInfo(dataPointsCount:Int):String{
        return dataPoints.entries.joinToString("\n") { (url, data) ->
            val relevantDataPoints = data.takeLast(dataPointsCount)
            val availability = getAvailability(relevantDataPoints)
            val avgResponseTime = relevantDataPoints.sumBy { it.responseTime.toInt() } / relevantDataPoints.size
            "Stats for $url: availability=$availability% average_response_time=$avgResponseTime"
        }
    }

    private fun getAvailability(data:List<TrackerDataPoint>):Int{
        return data.filter { it.responseCode.toString().startsWith("2") }.let {
            (it.size / data.size) * 100
        }
    }
}

data class TrackerDataPoint(val responseCode:Int?,val responseTime:Long)