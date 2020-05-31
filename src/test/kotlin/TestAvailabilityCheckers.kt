import availabilitycheckers.URLAvailabilityChecker
import java.net.URL

class HalfAvailableChecker:URLAvailabilityChecker {
    var isResAccepted = true
    override fun checkStatusCode(url: URL): Int? {
        return if(isResAccepted){
            isResAccepted = false
            200
        }else{
            isResAccepted = true
            null
        }
    }
}

class RecoveringAvailabilityChecker:URLAvailabilityChecker {
//    Dips below 80% availability after second checkStatusCode invocation then recovers to 81.81% after 11th call
//    The 12th call and later will always return null
    private val results = listOf(200,null,500,200,202,200,200,200,200,200,200)
    private var i = 0
    override fun checkStatusCode(url: URL): Int? {
        if(i > results.lastIndex){
            return null
        }
        return results[i].also { i++ }
    }
}