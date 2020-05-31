package availabilitycheckers

import java.net.URL

interface URLAvailabilityChecker{
//    Should return status code
    fun checkStatusCode(url: URL):Int?
}