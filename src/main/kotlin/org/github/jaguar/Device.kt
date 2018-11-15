package org.github.jaguar

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
interface Device {
    fun getInfo():String
    fun init()
    fun release()
    fun loop(f:()->Unit)
}