package org.github.jaguar

import org.github.jaguar.rendering.Device
import java.util.logging.Logger


/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
class Application(val device: Device){
    companion object {
        val LOG = Logger.getLogger(this::class.java.name)
    }
    fun run(){
        LOG.info("Start application with ${device.getInfo()}.")
        device.init()
        var lastFPS = 0
        device.loop { delta ->
            if(device.getFPS() != lastFPS) {
                lastFPS = device.getFPS()
            }
        }
        device.release()
    }
}