package org.github.jaguar

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
interface Device {
    var clearColor: Color
    fun getInfo():String
    fun init()
    fun release()
    fun loop(f:()->Unit)
}