package asteroids.test0;

import static org.lwjgl.system.MemoryStack.*;
import org.lwjgl.system.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Application {
	
	long window;
	
	public static void main(String[] args) {
		Application application = new Application();
	}
	
	public Application() {
		init();
		loop();
	}
	
	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, 0);
		glfwWindowHint(GLFW_RESIZABLE, 1);
		glfwWindowHint(GLFW_DECORATED, 0);
		window = glfwCreateWindow(1920/1, 1080/1, "Test", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(window, (vidMode.width()-pWidth.get(0))/2, (vidMode.height()-pHeight.get(0))/2);
		}
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
	}
	
	private void loop() {
		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		Game game = new Game();
		boolean connected = false;
		int jid = 0;
		for (int k = 0; k < 16; k++) {
			if (glfwJoystickPresent(k)) {
				connected = true;
				jid = k;
				break;
			}
		}
		while (!glfwWindowShouldClose(window)) {
			
			if (connected) {
				if (glfwJoystickPresent(jid)) {
					FloatBuffer axes = glfwGetJoystickAxes(jid);
					ByteBuffer buttons = glfwGetJoystickButtons(jid);
					if (buttons.get(0)>0) {
						game.ship.shoot();
					}
					game.ship.speed = -2000*axes.get(1)*2;
					game.ship.rotationSpeed = 3*Math.PI*axes.get(0)*2;
				}
			}
			
			glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
			glfwSetKeyCallback(window, new GLFWKeyCallback() {
				@Override
				public void invoke(long window, int key, int scancode, int action, int mods) {
					/*System.out.print(window);
					System.out.print("  ");
					System.out.print(key);
					System.out.print("  ");
					System.out.print(scancode);
					System.out.print("  ");
					System.out.print(action);
					System.out.print("  ");
					System.out.println(mods);*/
					if (key == GLFW_KEY_UP) {
						if (action == 1) {
							game.ship.speed = 1000;
						}
						if (action == 0) {
							game.ship.speed = 0;
						}
					}
					if (key == GLFW_KEY_DOWN) {
						if (action == 1) {
							game.ship.speed = -1000;
						}
						if (action == 0) {
							game.ship.speed = 0;
						}
					}
					if (key == GLFW_KEY_LEFT) {
						if (action == 1) {
							game.ship.rotationSpeed = -5;
						}
						if (action == 0) {
							game.ship.rotationSpeed = 0;
						}
					}
					if (key == GLFW_KEY_RIGHT) {
						if (action == 1) {
							game.ship.rotationSpeed = 5;
						}
						if (action == 0) {
							game.ship.rotationSpeed = 0;
						}
					}
					if (key == GLFW_KEY_SPACE) {
						if (action == 1) {
							game.ship.shoot();
						}
					}
				}
			});
			glColor3f(1, 1, 1);
			if (!game.ship.alive) {
				break;
			}
			System.out.println(game.ship.score);
			glLineWidth(2);
			drawGame(game);
			glfwSwapBuffers(window);
			glfwPollEvents();
			try {
				Thread.sleep(25);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			game.tick(0.025f);
		}
	}
	
	private void drawCircle(double posX, double posY, double radius, int edges, double offset) {
		double scope = 2*Math.PI;
		double d = scope/(double)edges;
		glBegin(GL_POINTS);
		Vertex(posX, posY);
		glEnd();
		glBegin(GL_LINES);
		for (double k = 0; k < 2*Math.PI; k+=d) {
			Vertex(radius*Math.sin(k+offset)+posX, radius*Math.cos(k+offset)+posY);
			Vertex(radius*Math.sin(k+offset+d)+posX, radius*Math.cos(k+offset+d)+posY);
		}
		glEnd();
	}
	private void drawFilledCircle(double posX, double posY, double radius, int edges, double offset) {
		double scope = 2*Math.PI;
		double d = scope/(double)edges;
		glBegin(GL_POINTS);
		Vertex(posX, posY);
		glEnd();
		glBegin(GL_POLYGON);
		for (double k = 0; k < 2*Math.PI; k+=d) {
			Vertex(radius*Math.sin(k+offset)+posX, radius*Math.cos(k+offset)+posY);
			//Vertex(radius*Math.sin(k+offset+d)+posX, radius*Math.cos(k+offset+d)+posY);
		}
		glEnd();
	}
	
	private void drawShip(double posX, double posY, double rotation) {
		glBegin(GL_POINTS);
		Vertex(posX, posY);
		glEnd();
		double edge1X = posX+30*Math.sin(rotation);
		double edge1Y = posY+30*Math.cos(rotation);
		double edge2X = posX+30*Math.sin(rotation+Math.toRadians(140));
		double edge2Y = posY+30*Math.cos(rotation+Math.toRadians(140));
		double edge3X = posX+30*Math.sin(rotation-Math.toRadians(140));
		double edge3Y = posY+30*Math.cos(rotation-Math.toRadians(140));
		glBegin(GL_LINES);
		Vertex(edge1X, edge1Y);
		Vertex(edge2X, edge2Y);
		Vertex(edge2X, edge2Y);
		Vertex(edge3X, edge3Y);
		Vertex(edge3X, edge3Y);
		Vertex(edge1X, edge1Y);
		glEnd();
	}
	
	private void drawGame(Game game) {
		drawShip(game.ship.posX, game.ship.posY, game.ship.rotation);
		for (Obstacle obstacle : game.obstacles) {
			drawCircle(obstacle.posX, obstacle.posY, obstacle.size*100, 12, obstacle.rotation);
		}
		for (Bullet bullet : game.bullets) {
			drawFilledCircle(bullet.posX, bullet.posY, 4, 6, 0);
		}
	}
	
	private void Vertex(double x, double y) {
		glVertex2d(x/1920, y/1080);
	}
}
