package org.github.jaguar

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */

fun main(args:Array<String>){
    Application(GLDevice(DeviceParameters(DeviceParameters.ScreenMode.WINDOWED,800,600,"Test"))).run()
}