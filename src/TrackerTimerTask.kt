import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TrackerTimerTask(websitesToTrack:List<URL>, private val interval:Int): TimerTask() {

    private var last1MinutePrint = System.currentTimeMillis()
    private var last10MinutePrint = System.currentTimeMillis()

    private val dataPoints:Map<URL,MutableList<TrackerDataPoint>> = websitesToTrack.map { it to mutableListOf<TrackerDataPoint>() }.toMap()
    private val maxDataPoints = 3600/interval

    override fun run() {
        updateDataPoints()

        printInfoIfNeeded()
    }

    private fun updateDataPoints(){
        dataPoints.entries.forEach { (url,data) ->
            if(data.size > maxDataPoints){
                data.drop(1)
            }

            val requestStartTime = System.currentTimeMillis()
            var responseCode:Int?

            with(url.openConnection() as HttpURLConnection){
                responseCode = this.responseCode
            }

            data.add(TrackerDataPoint(responseCode,System.currentTimeMillis()-requestStartTime))
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
            val availability = relevantDataPoints.filter { it.responseCode.toString().startsWith("2") }.let {
                (it.size / relevantDataPoints.size) * 100
            }
            val avgResponseTime = relevantDataPoints.sumBy { it.responseTime.toInt() } / relevantDataPoints.size
            "Stats for $url: availability=$availability% average_response_time=$avgResponseTime"
        }
    }
}

data class TrackerDataPoint(val responseCode:Int?,val responseTime:Long)