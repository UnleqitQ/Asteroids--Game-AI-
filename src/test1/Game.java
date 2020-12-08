package asteroids.test1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neat.calculate.Calculator;

public class Game {
	
	Ship ship;
	List<Bullet> bullets;
	List<Obstacle> obstacles;
	double totalTime;
	double timeSinceShot;
	Calculator calculator;
	Window window;
	
	double colorR = Math.random();
	double colorG = Math.random();
	double colorB = Math.random();
	
	public Game(Calculator calculator, Window window) {
		Map<Long, Float> inputMap = new HashMap<>();
		inputMap.put(0L, 1f);
		inputMap.put(1L, 1f);
		inputMap.put(2L, 0f);
		inputMap.put(3L, 0f);
		inputMap.put(4L, 0f);
		inputMap.put(5L, 0f);
		inputMap.put(6L, 0f);
		inputMap.put(7L, 1f);
		calculator.setInputValues(inputMap);
		Map<Long, Float> outputMap = calculator.getOutputs();
		colorR = outputMap.get(8L)*100;
		colorG = outputMap.get(9L)*100;
		colorB = outputMap.get(10L)*100;
		colorR = colorR - Math.floor(colorR);
		colorG = colorG - Math.floor(colorG);
		colorB = colorB - Math.floor(colorB)+Math.max(0, 0.4-(colorR+colorG));
		this.calculator = calculator;
		this.window = window;
		ship = new Ship(this, 0, 0);
		timeSinceShot = 100;
		obstacles = new ArrayList<>();
		bullets = new ArrayList<>();
		for (int l = 0; l < 15; l++) {
			obstacles.add(new Obstacle(this, Math.random()/2+(Math.random()<0.5?0.5:-1)*1920, Math.random()/2+(Math.random()<0.5?0.5:-1)*1080, Math.random()*2*Math.PI, 500*Math.random(), Math.floor(Math.random()*2)+1));
		}
	}
	
	public void tick(float dt) {
		if (!ship.alive) {
			return;
		}
		ship.tick(dt);
		totalTime += dt;
		timeSinceShot += dt;
		List<Bullet> removeBullets = new ArrayList<>();
		for (Bullet bullet : bullets) {
			bullet.tick(dt);
			if (!bullet.alive) {
				removeBullets.add(bullet);
			}
		}
		List<Obstacle> removeObstacles = new ArrayList<>();
		for (Obstacle obstacle : obstacles) {
			obstacle.tick(dt);
			obstacle.test();
			if (!obstacle.alive) {
				removeObstacles.add(obstacle);
			}
		}
		ship.test();
		ship.think();
		for (Obstacle obstacle : removeObstacles) {
			obstacles.remove(obstacle);
			obstacle.createChildren(3);
		}
		for (Bullet bullet : removeBullets) {
			bullets.remove(bullet);
		}
		if (obstacles.size()<15) {
			Obstacle obstacle = new Obstacle(this, Math.random()/2+(Math.random()<0.5?0.5:-1)*1920, Math.random()/2+(Math.random()<0.5?0.5:-1)*1080, Math.random()*2*Math.PI, 500*Math.random(), Math.floor(Math.random()*2)+1);
			if (Math.sqrt(Math.pow(obstacle.posX-ship.posX, 2)+Math.pow(obstacle.posY-ship.posY, 2))>=obstacle.size*1.3) {
				obstacles.add(obstacle);
			}
		}
	}
	
}
