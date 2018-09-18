import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class Main {
    companion object {
        private const val MA_PORT = 6969

        private const val KEK = "KEK"

        private val clientThreadPool = ArrayList<ClientThread>()

        private var id = 0

        val clientInteraction = object : ClientThread.ClientInteraction {
            override fun disconected(id: Int) {
                clientThreadPool.forEach {
                    it.removeClient(id)
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            println("Start server desktop application.")
            try {

                val server = ServerSocket(MA_PORT)
                val inputServerStreamReader = BufferedReader(InputStreamReader(System.`in`))

                println("Server is created.")

                while (true) {   //!server.isClosed) {
                    /*if (inputServerStreamReader.ready()) {
                        val client = server.accept()
                        println("Client is found")
                        ClientThread(client, id++)
                    }*/
                    val socket = server.accept()
                    try {
                        id++
                        val listOfCliets = StringBuilder()
                        clientThreadPool.forEach {
                            it.addClient(id)
                            listOfCliets.append(it.id).append(" ")
                        }
                        val clientThread = ClientThread(socket, id, clientInteraction)
                        clientThread.setListOfClients(listOfCliets.toString())
                        clientThreadPool.add(clientThread)
                    } catch (e: Exception) {
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}