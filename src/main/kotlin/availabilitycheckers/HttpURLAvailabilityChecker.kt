package availabilitycheckers

import java.net.HttpURLConnection
import java.net.URL

class HttpURLAvailabilityChecker():URLAvailabilityChecker{
    override fun checkStatusCode(url: URL): Int? {
        with(url.openConnection() as HttpURLConnection){
            return this.responseCode
        }
    }
}