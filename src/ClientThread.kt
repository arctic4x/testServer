import java.io.*
import java.net.Socket

private const val IN_EXIT = "EXIT"
private const val IN_MESSAGE = "MESSAGE"

private const val OUT_MESSAGE = "MESSAGE"
private const val OUT_LIST_OF_CLIENTS = "LIST_OF_CLIENTS"
private const val OUT_ADD_CLIENT = "ADD_CLIENT"
private const val OUT_REMOVE_CLIENT = "REMOVE_CLIENT"

class ClientThread(val socket: Socket, val id: Int, val clientInteraction: ClientInteraction) : Thread() {
    private val readerStream: BufferedReader
    private val writerStream: PrintWriter

    private var isRunning = true

    init {
        println("Client ${id} connected to socket")
        readerStream = BufferedReader(InputStreamReader(socket.getInputStream()))
        writerStream = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())))

        sendMessage("Hello!!! you are welcome")

        start()
    }

    override fun run() {
        try {
            while (isRunning) {
                val command = readerStream.readLine()

                println("Command: ${command}")

                when (command) {
                    IN_EXIT -> isRunning = false
                    IN_MESSAGE -> sendMessage(readerStream.readLine())
                }
            }

            clientInteraction.disconected(id)
            println("Client ${id} disconnected")
        } catch (e: Exception) {
        }
    }

    fun sendMessage(message: String) {
        writerStream.println(OUT_MESSAGE)
        writerStream.println(message)
        writerStream.flush()
    }

    fun setListOfClients(listOfClients: String) {
        writerStream.println(OUT_LIST_OF_CLIENTS)
        writerStream.println(listOfClients)
        writerStream.flush()
    }

    fun addClient(id: Int) {
        writerStream.println(OUT_ADD_CLIENT)
        writerStream.println(id.toString())
        writerStream.flush()
    }
    fun removeClient(id: Int) {
        writerStream.println(OUT_REMOVE_CLIENT)
        writerStream.println(id.toString())
        writerStream.flush()
    }

    interface ClientInteraction{
        fun disconected(id: Int)
    }
}