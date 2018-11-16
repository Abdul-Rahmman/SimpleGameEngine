package org.github.jaguar

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
    override var clearColor: Color = Color(0f,0f,0f,0f)
    private var window: Long = MemoryUtil.NULL

    override fun loop(f: () -> Unit) {
        GL.createCapabilities()
        GL11.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            GLFW.glfwSwapBuffers(window)

            GLFW.glfwPollEvents()
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
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR,0)
        //GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_CORE_PROFILE,GLFW.GLFW_TRUE)

        window = GLFW.glfwCreateWindow(deviceParameters.screenWidth, deviceParameters.screenHeight, deviceParameters.windowTitle, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL)
            throw RuntimeException("Failed to create the GLFW window")

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

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)?.free()
    }


}