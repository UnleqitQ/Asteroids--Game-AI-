package asteroids.test1;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;

public class Ship {
	
	double posX;
	double posY;
	double rotation;
	double speed;
	double rotationSpeed;
	double score;
	boolean alive;
	
	Game game;
	
	public Ship(Game game, double posX, double posY) {
		game.calculator.genome.fitness = 0;
		this.game = game;
		this.posX = posX;
		this.posY = posY;
		speed = 0;
		score = 0;
		alive = true;
	}
	
	public void shoot() {
		if (game.timeSinceShot<=0) {
			return;
		}
		Bullet bullet = new Bullet(this, posX, posY, rotation);
		game.bullets.add(bullet);
		game.timeSinceShot = Math.min(0, game.timeSinceShot-0.2);
	}
	
	public void tick(float dt) {
		posX += Math.sin(rotation)*speed*dt;
		posY += Math.cos(rotation)*speed*dt;
		rotation += rotationSpeed*dt;
		posX = posX-(Math.floor((posX+1920)/1920/2))*1920*2;
		posY = posY-(Math.floor((posY+1080)/1080/2))*1080*2;
		game.calculator.genome.fitness = (float) (score+game.totalTime/1000);
	}
	
	public boolean test() {
		if (!alive) {
			return true;
		}
		for (Obstacle obstacle : game.obstacles) {
			if (!obstacle.alive) {
				continue;
			}
			if (Math.sqrt(Math.pow(obstacle.posX-posX, 2)+Math.pow(obstacle.posY-posY, 2))<=obstacle.size*100+20) {
				alive = false;
				/*if (game.application.show) {
					glColor3f(1, 0, 0);
					game.application.drawCircle(posX, posY, 40, 20, 0);
				}*/
				return true;
			}
		}
		return false;
	}
	
	public void think() {
		Map<Long, Float> inputMap = getInputMap();
		game.calculator.setInputValues(inputMap);
		Map<Long, Float> outputMap = game.calculator.getOutputs();
		acceptOutputs(outputMap);
	}
	
	private void acceptOutputs(Map<Long, Float> outputMap) {
		int inputSize = 8;
		speed = Math.max(-2000, Math.min(2000, 1000*(outputMap.get((long)inputSize))*2-1));
		rotationSpeed = Math.max(-Math.PI*2, Math.min(Math.PI*2, 1*Math.PI*(outputMap.get((long)inputSize+1))*2-1));
		if (outputMap.get((long)(inputSize+2))>=0.5) {
			shoot();
		}
	}
	
	private Map<Long, Float> getInputMap() {
		Map<Long, Float> inputMap = new HashMap<>();
		inputMap.put(0L, 100f);
		inputMap.put(1L, 100f);
		inputMap.put(2L, 100f);
		inputMap.put(3L, 100f);
		inputMap.put(4L, 100f);
		inputMap.put(5L, 100f);
		inputMap.put(6L, 100f);
		inputMap.put(7L, 100f);
		double offset = Math.toRadians(0);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(0L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(45);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(1L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(90);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(2L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(135);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(3L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(180);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(4L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(-135);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(5L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(-90);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(6L, (float)k);
				break;
			}
		}
		offset = Math.toRadians(-45);
		for (int k = 0; k < 100; k++) {
			Tester tester = new Tester(this, posX+Math.cos(rotation+offset)*k*10, posY+Math.sin(rotation+offset)*k*10);
			if (tester.test()) {
				inputMap.put(7L, (float)k);
				break;
			}
		}
		return inputMap;
	}
	
}
