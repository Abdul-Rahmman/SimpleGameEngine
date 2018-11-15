package org.github.jaguar

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
class DeviceParameters(val screenMode: ScreenMode,
                       val screenWidth:Int,
                       val screenHeight: Int,
                       val windowTitle: String = "",
                       val vsync: Boolean = true) {
    enum class ScreenMode{
        FULLSCREEN,
        WINDOWED_FULLSCREEN,
        WINDOWED
    }
}