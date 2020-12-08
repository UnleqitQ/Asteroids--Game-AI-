package asteroids.test1;

import static org.lwjgl.opengl.GL11.*;

public class Tester {
	
	double posX;
	double posY;
	Ship ship;
	
	public Tester(Ship ship, double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
		this.ship = ship;
		posX = posX-(Math.floor((posX+1920)/1920/2))*1920*2;
		posY = posY-(Math.floor((posY+1080)/1080/2))*1080*2;
	}
	
	public boolean test() {
		for (Obstacle obstacle : ship.game.obstacles) {
			if (Math.sqrt(Math.pow(obstacle.posX-posX, 2)+Math.pow(obstacle.posY-posY, 2))<=obstacle.size*100) {
				if (((ApplicationAsteroidsT1)ship.game.window).show) {
					glColor3f(05.f, 0, 0);
					glLineWidth(0.1f);
					//ship.game.application.drawFilledCircle(posX, posY, 4, 4, 0);
					glBegin(GL_LINES);
					ship.game.window.Vertex(posX, posY);
					ship.game.window.Vertex(ship.posX, ship.posY);
					glEnd();
				}
				return true;
			}
		}
		return false;
	}
	
}
