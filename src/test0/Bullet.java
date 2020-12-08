package asteroids.test0;

public class Bullet {
	
	double posX;
	double posY;
	double livingTime;
	double rotation;
	double speed;
	boolean alive;
	Ship ship;
	
	public Bullet(Ship ship, double posX, double posY, double rotation) {
		this.posX = posX;
		this.posY = posY;
		this.rotation = rotation;
		speed = 2000;
		livingTime = 0;
		alive = true;
		this.ship = ship;
	}
	
	public void tick(float dt) {
		livingTime += dt;
		posX += Math.sin(rotation)*speed*dt;
		posY += Math.cos(rotation)*speed*dt;
		posX = posX-(Math.floor((posX+1920)/1920/2))*1920*2;
		posY = posY-(Math.floor((posY+1080)/1080/2))*1080*2;
		if (livingTime >= 3) {
			alive = false;
		}
	}
	
}
