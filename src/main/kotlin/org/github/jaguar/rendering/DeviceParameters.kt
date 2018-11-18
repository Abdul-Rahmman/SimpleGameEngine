package org.github.jaguar.rendering

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
open class DeviceParameters(val screenMode: ScreenMode,
                       val screenWidth:Int,
                       val screenHeight: Int,
                       val windowTitle: String = "",
                       val vsync: Boolean = true,
                       val resizeableWindow: Boolean = false) {
    enum class ScreenMode{
        FULLSCREEN,
        WINDOWED_NOBORDER,
        WINDOWED
    }
}