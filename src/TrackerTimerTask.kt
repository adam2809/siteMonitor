import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TrackerTimerTask(websitesToTrack:List<URL>,interval:Long): TimerTask() {

    private var ticks = 0

    private val dataPoints:Map<URL,MutableList<TrackerDataPoint>> = websitesToTrack.map { it to mutableListOf<TrackerDataPoint>() }.toMap()
    private val maxDataPoints = 3600/interval

    override fun run() {
        dataPoints.entries.forEach { (url,data) ->
            println("Polling $url")

            if(data.size > maxDataPoints){
                data.drop(1)
            }

            val requestStartTime = System.currentTimeMillis()
            var responseCode:Int?

            with(url.openConnection() as HttpURLConnection){
                responseCode = this.responseCode
            }

            data.add(TrackerDataPoint(responseCode,System.currentTimeMillis()-requestStartTime))
            println("The result was ${TrackerDataPoint(responseCode,System.currentTimeMillis()-requestStartTime)}")
        }

        ticks++
    }
}

data class TrackerDataPoint(val responseCode:Int?,val responseTime:Long)