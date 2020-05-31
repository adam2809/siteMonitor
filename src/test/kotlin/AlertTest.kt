import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream


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
        fail("Not implemented")
    }

    @Test
    fun `check if user is alerted about a website going back up`(){
        fail("Not implemented")
    }
}