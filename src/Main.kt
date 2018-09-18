import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class Main {
    companion object {
        private const val MA_PORT = 6969

        private const val KEK = "KEK"

        private val clientThreadPool = ArrayList<Thread>()

        private var id = 0

        @JvmStatic
        fun main(args: Array<String>) {
            println("Start server desktop application.")
            try {

                val server = ServerSocket(MA_PORT)
                val inputServerStreamReader = BufferedReader(InputStreamReader(System.`in`))

                println("Server is created.")

                while (true){   //!server.isClosed) {
                    /*if (inputServerStreamReader.ready()) {
                        val client = server.accept()
                        println("Client is found")
                        ClientThread(client, id++)
                    }*/
                    val socket = server.accept()
                    try {
                        ClientThread(socket, id++)
                    }catch (e : Exception){}
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}