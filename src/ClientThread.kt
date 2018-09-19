import java.io.*
import java.net.Socket

const val IN_EXIT = "EXIT"
const val IN_MESSAGE = "MESSAGE"
const val IN_MESSAGE_TO_CLIENT = "MESSAGE_TO_CLIENT"

const val OUT_MESSAGE = "MESSAGE"
const val OUT_LIST_OF_CLIENTS = "LIST_OF_CLIENTS"
const val OUT_ADD_CLIENT = "ADD_CLIENT"
const val OUT_REMOVE_CLIENT = "REMOVE_CLIENT"
const val OUT_MESSAGE_FROM_CLIENT = "MESSAGE_FROM_CLIENT"

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
                if (command==null) isRunning = false

                println("Command: ${command}")

                when (command) {
                    IN_EXIT -> isRunning = false
                    IN_MESSAGE -> sendMessage(readerStream.readLine())
                    IN_MESSAGE_TO_CLIENT -> sendMessageToClient()
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
        if (listOfClients.isNotBlank()) {
            println("Send to client $id list: $listOfClients")
            writerStream.println(OUT_LIST_OF_CLIENTS)
            writerStream.println(listOfClients)
            writerStream.flush()
        }
    }

    fun addClient(id: Int) {
        println("Send to client ${this.id} add client id: $id")
        writerStream.println(OUT_ADD_CLIENT)
        writerStream.println(id.toString())
        writerStream.flush()
    }

    fun removeClient(id: Int) {
        println("Send to client ${this.id} remove client id: $id")
        writerStream.println(OUT_REMOVE_CLIENT)
        writerStream.println(id.toString())
        writerStream.flush()
    }

    fun sendMessageToClient() {
        var clientId = 0
        try {
            clientId = Integer.parseInt(readerStream.readLine())
        } catch (e: NumberFormatException) {
            clientId = -1
        }
        val message = readerStream.readLine()
        println("Send to client ${this.id} send message: $message to client $clientId")

        clientInteraction.messageToClient(message, clientId, id)
    }

    fun sendMessageToClient(message: String, fromClientId: Int) {
        println("Sending to client ${this.id} message: $message from ${fromClientId}")

        writerStream.println(OUT_MESSAGE_FROM_CLIENT)
        writerStream.println(fromClientId)
        writerStream.println(message)
        writerStream.flush()
    }

    interface ClientInteraction {
        fun disconected(id: Int)
        fun messageToClient(message: String, toClientId: Int, fromClientId: Int)
    }
}