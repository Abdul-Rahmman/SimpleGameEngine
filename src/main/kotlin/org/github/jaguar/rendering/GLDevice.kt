package org.github.jaguar.rendering

import org.github.jaguar.Color
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

/**
 * @author Ермаков Игорь Александрович (email: igor.yermakov94@yandex.ru).
 */
class GLDevice(val deviceParameters: DeviceParameters) : Device {
    override var clearColor: Color = Color(0f, 0f, 0f, 0f)
    private var window: Long = MemoryUtil.NULL
    private var fps: Int = 0
    private var delta = 1

    override fun loop(f: (Int) -> Unit) {
        GL.createCapabilities()
        GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
        GL11.glClearDepth( 1.0 )              // Разрешить очистку буфера глубины
        GL11.glEnable( GL11.GL_DEPTH_TEST )            // Разрешить тест глубины
        GL11.glDepthFunc( GL11.GL_LEQUAL )

        var start: Long
        while (!GLFW.glfwWindowShouldClose(window)) {
            start = System.currentTimeMillis()
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            f(delta)
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
            delta = (System.currentTimeMillis() - start).toInt()
            if(delta != 0)
                fps = (1.0 / delta * 1000).toInt()
        }
    }


    override fun getInfo(): String {
        return "OpenGL"
    }

    override fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!GLFW.glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, if(deviceParameters.resizeableWindow )GLFW.GLFW_TRUE else GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR,0)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE,GLFW.GLFW_OPENGL_CORE_PROFILE)
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)

        var monitor = MemoryUtil.NULL
        if(deviceParameters.screenMode == DeviceParameters.ScreenMode.FULLSCREEN)
            monitor = GLFW.glfwGetPrimaryMonitor()

        window = GLFW.glfwCreateWindow(deviceParameters.screenWidth, deviceParameters.screenHeight, deviceParameters.windowTitle, monitor, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate()
            throw RuntimeException("Failed to create the GLFW window")
        }

        if(deviceParameters.screenMode == DeviceParameters.ScreenMode.WINDOWED_NOBORDER){
            monitor = GLFW.glfwGetPrimaryMonitor()
            val mode = GLFW.glfwGetVideoMode(monitor)
            if(mode == null){
                GLFW.glfwTerminate()
                throw IllegalStateException("Video Mode is not configured")
            }

            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, mode.redBits())
            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, mode.greenBits())
            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, mode.blueBits())
            GLFW. glfwWindowHint(GLFW.GLFW_REFRESH_RATE, mode.refreshRate())
            GLFW.glfwSetWindowMonitor(window,monitor,0,0,deviceParameters.screenWidth,deviceParameters.screenHeight,GLFW.GLFW_DONT_CARE)
        }

        GLFW.glfwSetKeyCallback(window) { wnd, key, _, action, _ ->
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(wnd, true)
        }

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            val videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
                    ?: throw java.lang.IllegalStateException("Invalid video mode")

            GLFW.glfwSetWindowPos(
                    window,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            )
        }

        GLFW.glfwMakeContextCurrent(window)
        GLFW.glfwSwapInterval(1)
        GLFW.glfwShowWindow(window)
    }

    override fun release() {
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }

    override fun getFPS(): Int {
        return fps
    }

    override fun getDelta(): Int {
        return delta
    }
}