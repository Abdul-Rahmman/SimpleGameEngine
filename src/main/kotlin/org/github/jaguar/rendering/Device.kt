package org.github.jaguar.rendering

import org.github.jaguar.Color

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
interface Device {
    var clearColor: Color
    fun getInfo():String
    fun init()
    fun release()
    fun loop(f:(Int)->Unit)
    fun getFPS():Int
    fun getDelta():Int
}