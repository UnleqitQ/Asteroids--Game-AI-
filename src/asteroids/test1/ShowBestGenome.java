package asteroids.test1;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.neat.activationFunction.ActivationFunction;
import org.neat.calculate.Calculator;
import org.neat.neat.Neat;

public class ShowBestGenome implements Runnable {
	
	
	int generation = 0;
	
	Game game;
	
	ApplicationAsteroidsT1 applicationAsteroidsT1;
	
	public ShowBestGenome(ApplicationAsteroidsT1 applicationAsteroidsT1) {
		this.applicationAsteroidsT1 = applicationAsteroidsT1;
	}
	
	@Override
	public void run() {
		loop();
	}
	
	public void loop() {
		while (applicationAsteroidsT1.bestGenomes.size()<1) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
		while (true) {
			System.out.println("h0");
			boolean run = true;
			Calculator calculator = new Calculator(applicationAsteroidsT1.bestGenomes.get(generation));
			calculator.initialize();
			game = new Game(calculator, applicationAsteroidsT1);
			while (run) {
				System.out.println("h");
				run = game.ship.alive;
				game.tick(0.02f);
				try {
					Thread.sleep(2);
				}
				catch (InterruptedException ex) {
					ex.printStackTrace();
					return;
				}
			}
		}
	}
	
}
