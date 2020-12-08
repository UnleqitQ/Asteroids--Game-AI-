package asteroids.test1;

public class Obstacle {
	
	double posX;
	double posY;
	double rotation;
	double speed;
	double size;
	boolean alive;
	
	Game game;
	
	public Obstacle(Game game, double posX, double posY, double rotation, double speed, double size) {
		this.game = game;
		this.posX = posX;
		this.posY = posY;
		this.rotation = rotation;
		this.speed = speed*4;
		this.size = size;
		alive = true;
	}
	
	public void tick(float dt) {
		posX += Math.cos(rotation)*speed*dt;
		posY += Math.sin(rotation)*speed*dt;
		posX = posX-(Math.floor((posX+1920)/1920/2))*1920*2;
		posY = posY-(Math.floor((posY+1080)/1080/2))*1080*2;
	}
	
	public void test() {
		if (!alive) {
			return;
		}
		for (Bullet bullet : game.bullets) {
			if (!bullet.alive) {
				continue;
			}
			if (Math.sqrt(Math.pow(bullet.posX-posX, 2)+Math.pow(bullet.posY-posY, 2))<=size*100) {
				bullet.alive = false;
				game.bullets.remove(bullet);
				bullet.ship.score+=1/size;
				alive = false;
				return;
			}
		}
	}
	
	public void createChildren(int count) {
		if (size == 1) {
			return;
		}
		for (int k = 0; k < count; k++) {
			game.obstacles.add(new Obstacle(game, posX+Math.random()*200-100, posY+Math.random()*200-100, Math.random()*2*Math.PI, 500*Math.random(), size-1));
		}
	}
	
}
