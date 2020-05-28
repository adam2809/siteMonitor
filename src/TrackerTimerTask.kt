import java.net.URL
import java.util.*

class TrackerTimerTask(websitesToTrack:List<URL>): TimerTask() {
    
    override fun run() {
        println("Tracing like crazy!!!")
    }
}