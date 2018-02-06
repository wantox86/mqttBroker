
import io.moquette.server.Server
import java.util.Arrays
import io.moquette.server.config.ClasspathConfig
import io.moquette.interception.messages.InterceptPublishMessage
import io.moquette.interception.AbstractInterceptHandler
import io.moquette.interception.InterceptHandler


fun main(args: Array<String>)
{
    val classPathConfig = ClasspathConfig()

    val mqttBroker = Server()
    val userHandlers = Arrays.asList<Any>(PublisherListener())
    mqttBroker.startServer(classPathConfig, userHandlers as MutableList<out InterceptHandler>?)

    println("moquette mqtt broker started, press ctrl-c to shutdown..")
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            println("stopping moquette mqtt broker..")
            mqttBroker.stopServer()
            println("moquette mqtt broker stopped")
        }
    })
}


internal class PublisherListener : AbstractInterceptHandler() {
    override fun onPublish(message: InterceptPublishMessage?) {
        println("moquette mqtt broker message intercepted, topic: " + message!!.topicName
                + ", content: " + String(message.payload.array()))
    }
}