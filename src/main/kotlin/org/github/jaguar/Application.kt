package org.github.jaguar

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
        device.loop {  }
        device.release()
    }
}