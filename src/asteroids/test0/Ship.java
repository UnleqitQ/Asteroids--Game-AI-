package asteroids.test0;

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
		this.game = game;
		this.posX = posX;
		this.posY = posY;
		speed = 0;
		score = 0;
		alive = true;
	}
	
	public void shoot() {
		if (game.timeSinceShot<0) {
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
	}
	
	public void test() {
		if (!alive) {
			return;
		}
		for (Obstacle obstacle : game.obstacles) {
			if (!obstacle.alive) {
				continue;
			}
			if (Math.sqrt(Math.pow(obstacle.posX-posX, 2)+Math.pow(obstacle.posY-posY, 2))<=obstacle.size*100+20) {
				alive = false;
				return;
			}
		}
	}
	
}
