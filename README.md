##Installation

Run `git clone https://github.com/adam2809/siteMonitor.git` in the directory you  would like to store the source.

##Running and usage

To run the program execute `./gradlew run` in the projects root folder. 
The application.properties (which is stored in src/main/resources) file is loaded with some default websites but if you
wish to add your own run `./gradlew run args="-a newwebsite.com"` or edit the file manually. The program will alert
you if a website is down for 2 or more minutes (less than 80% availability) and it will print some metrics every
1 minute (metrics for the past 10 minutes) and every 10 minutes (metrics for the past 1 hour). You will also get
notified if availability goes back to above 80%.

##Ideas for better design

* The alerts should be distinguished from the info prints (for example bold and red).

* The alerts should not only be printed but also send a notification which can be done using libjava-gnome-java.

* A dashboard could be created by sticking to the terminal but using Lanterna, Charva, or Java Curses or a web-based
interface which could be easily implemented with any JVM Web Framework and any frontend framework by either converting
the codebase to Kotlin JS or simply taking the TrackerTimerTask and rerouting println from stdout to sending new data to
the frontend client.

* The app.properties file does not support storing lists so a better format should be used such as JSON which does
support it. I decided to use a built in Java solution to avoid using a dependency which seems unnecessary for
this project considering its scope.

* `TrackerTimerTask.updateDataPoints TrackerTimerTask.printInfoIfNeeded TrackerTimerTask.alertIfNeeded TrackerTimerTask.alertRecoveryIfNeeded`
each of these methods go through the map of URL to TrackerDataPoint which is inefficient. This is not an issue considering
the scope of the project but a top level loop should be implemented for further expansion.
