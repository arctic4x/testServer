import java.io.*
import java.net.Socket

private const val EXIT = "EXIT"

class ClientThread(val socket: Socket, val id: Int) : Thread() {
    private val readerStream: BufferedReader
    private val writerStream: PrintWriter
    private var isRunning = true

    init {
        println("Client ${id} connected to socket")
        readerStream = BufferedReader(InputStreamReader(socket.getInputStream()))
        writerStream = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())))

        writerStream.println("Hello!!! you are welcome")
        writerStream.flush()

        start()
    }

    override fun run() {
        try {
            while (isRunning) {
                val command = readerStream.readLine()

                println("Command: ${command}")

                when (command) {
                    EXIT -> isRunning = false
                    else -> {
                        writerStream.println(command)
                        writerStream.flush()
                    }
                }
            }

            println("Client ${id} disconnected")
        } catch (e: Exception) {
        }
    }
}