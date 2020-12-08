package asteroids.test0;

import java.util.ArrayList;
import java.util.List;

public class Game {
	
	Ship ship;
	List<Bullet> bullets;
	List<Obstacle> obstacles;
	double totalTime;
	double timeSinceShot;
	
	public Game() {
		ship = new Ship(this, 0, 0);
		timeSinceShot = 100;
		obstacles = new ArrayList<>();
		bullets = new ArrayList<>();
		for (int l = 0; l < 10; l++) {
			obstacles.add(new Obstacle(this, Math.random()/2+(Math.random()<0.5?0.5:-1)*1920, Math.random()/2+(Math.random()<0.5?0.5:-1)*1080, Math.random()*2*Math.PI, 500*Math.random(), Math.floor(Math.random()*3)+1));
		}
	}
	
	public void tick(float dt) {
		ship.tick(dt);
		totalTime += dt;
		timeSinceShot += dt*2;
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
		for (Obstacle obstacle : removeObstacles) {
			obstacles.remove(obstacle);
			obstacle.createChildren(3);
		}
		for (Bullet bullet : removeBullets) {
			bullets.remove(bullet);
		}
		if (obstacles.size()<10) {
			Obstacle obstacle = new Obstacle(this, Math.random()/2+(Math.random()<0.5?0.5:-1)*1920, Math.random()/2+(Math.random()<0.5?0.5:-1)*1080, Math.random()*2*Math.PI, 500*Math.random(), Math.floor(Math.random()*3)+1);
			if (Math.sqrt(Math.pow(obstacle.posX-ship.posX, 2)+Math.pow(obstacle.posY-ship.posY, 2))>=obstacle.size*1.3) {
				obstacles.add(obstacle);
			}
		}
	}
	
}
