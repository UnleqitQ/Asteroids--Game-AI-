package asteroids.test1;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	long window;
	
	
	
	public Window() {
	}
	
	
	public void drawCircle(double posX, double posY, double radius, int edges, double offset) {
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
	public void drawFilledCircle(double posX, double posY, double radius, int edges, double offset) {
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
	
	public void drawShip(double posX, double posY, double rotation) {
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
	
	public void drawGame(Game game) {
		glColor3d(game.colorR, game.colorG, game.colorB);
		if (!game.ship.alive) {
			return;
		}
		drawShip(game.ship.posX, game.ship.posY, game.ship.rotation);
		for (Obstacle obstacle : game.obstacles) {
			drawCircle(obstacle.posX, obstacle.posY, obstacle.size*100, 12, obstacle.rotation);
		}
		for (Bullet bullet : game.bullets) {
			drawFilledCircle(bullet.posX, bullet.posY, 4, 6, 0);
		}
	}
	
	public void Vertex(double x, double y) {
		glVertex2d(x/1920, y/1080);
	}
}
