import org.junit.jupiter.api.*
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import java.net.URL

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlertTest{

    private val stdOut = ByteArrayOutputStream()

    @BeforeAll
    fun redirectStdOut(){
        System.setOut(PrintStream(stdOut))
    }

    @AfterAll
    fun restoreStdOut(){
        System.setOut(PrintStream(FileOutputStream(FileDescriptor.out)));
    }

    @Test
    fun `check if user is alerted about a website going down`(){
        val task = TrackerTimerTask(listOf(URL("https://doesntMatterForTesting.com")),30,HalfAvailableChecker())
        repeat(4) { task.run() }
        assert(stdOut.toString().contains("Website https://doesntMatterForTesting.com is down."))
        assert(!stdOut.toString().contains("Website https://doesntMatterForTesting.com is back up."))
    }

    @Test
    fun `check if user is alerted about a website going back up`(){
        val task = TrackerTimerTask(listOf(URL("https://doesntMatterForTesting.com")),30,RecoveringAvailabilityChecker())
        repeat(4) { task.run() }
        assert(stdOut.toString().contains("Website https://doesntMatterForTesting.com is down."))
        repeat(7) { task.run() }
        assert(stdOut.toString().contains("Website https://doesntMatterForTesting.com is back up."))
    }
}