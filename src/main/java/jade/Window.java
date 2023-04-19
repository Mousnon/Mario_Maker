package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
   private int width, height;
   private String title;
   private long glfwWindow;

   private float r, g, b, a;

   private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Cursed Mario BROS.U";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
    if (Window.window == null) {
        Window.window = new Window();
    }
    return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();


        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private boolean fadeToBlack = false;

    public void init() {
    //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to Initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create the Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW Window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the Window Visible
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop() {
  float beginTime = Time.getTime();
  float endTime = Time.getTime();

   while (!glfwWindowShouldClose(glfwWindow)) {
       // Poll events
       glfwPollEvents();

       glClearColor(r, g, b, a);
       glClear(GL_COLOR_BUFFER_BIT);

       if (fadeToBlack) {
           r = Math.max(r - 0.01f, 0);
           g = Math.max(g - 0.01f, 0);
           b = Math.max(b - 0.01f, 0);
       }

       if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
          fadeToBlack = true;
       }

       glfwSwapBuffers(glfwWindow);

       endTime = Time.getTime();
       float dt = endTime - beginTime;
       beginTime = endTime;
   }
    }
}
