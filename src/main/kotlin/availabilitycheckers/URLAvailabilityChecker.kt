package availabilitycheckers

import java.net.URL

interface URLAvailabilityChecker{
    fun checkStatusCode(url: URL):Int?
}