package org.projectargus.parts.attack;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.objects.Ship;
import org.projectargus.parts.PartSlot;
import org.projectargus.states.FightState;

public class Missle extends Attack
{
	int amount = 12;
	int time = 250;
	int current = time;

	Image img;
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Missle()
	{
		super();
		
		try {
			img = new Image("res/img/missile.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fire(PartSlot slot, ArrayList<Ship> targets)
	{
		System.out.println("Firing at " + targets.size() + " ships.");
		bullets.clear();
		this.slot = slot;
		this.targets = targets;
		state = State.FIRING;
		
		for(int i = 0;i<amount;i++)
		{
			int tempShipInt = i % targets.size();
			Ship target = targets.get(tempShipInt);
			bullets.add(new Bullet((int)slot.getX(),(int)slot.getY(),target,1,img));
		}
	}
	
	@Override
	public void firingRender(GameContainer gc, StateBasedGame sbg, Graphics g)
	{
		for(int i = 0 ;i<bullets.size();i++)
		{
			if(bullets.get(i).launched && bullets.get(i).done == false)
			{
				bullets.get(i).render(gc, sbg, g);
			}
			else if(bullets.get(i).launched && bullets.get(i).done)
			{
				bullets.get(i).explosion.render(gc,sbg,g);
			}
		}			
	}
	
	@Override
	public void firingUpdate(GameContainer gc, StateBasedGame sbg, int delta,Rectangle screenRect) 
	{
		if(isDone())
		{
			state = State.DONE;
			bullets.clear();
		}
		
		current += delta;

		if(current >= time)
		{
			current = 0;
			
			for(int i = 0;i<bullets.size();i++)
			{
				if(bullets.get(i).launched == false && bullets.get(i).done == false)
				{
					bullets.get(i).setPosition((int)slot.getX(),(int)slot.getY());
					bullets.get(i).launched = true;
					break;
				}				
			}
		}
		
		for(int i = 0 ;i<bullets.size();i++)
		{
			if(bullets.get(i).launched)
			{
				bullets.get(i).update(gc, sbg, delta, screenRect);
			}
		}	
	}
	
	public boolean isDone()
	{
		for(int i = 0;i<bullets.size();i++)
		{
			if(bullets.get(i).isDone() == false || bullets.get(i).launched == false)
			{
				return false;
			}else{
				bullets.remove(i);
			}
		}
		return true;
	}
	
	private class Bullet
	{
		float x;
		float y;

		float w = 50;
		float h = 25;

		Rectangle screenDims;

		Ship target;
		
		//float angle;
		float speed;

		boolean launched = false;
		boolean done = false;
		
		
		//TODO remove screen pos some point
		float screen_x;
		float screen_y;
		float screen_w;
		float screen_h;

		Image img;

		public Explosion explosion;
		

		public Bullet(int x,int y,Ship target,float speed,Image img)
		{
			this.x = x;
			this.y = y;			
			this.target = target;
			this.speed = speed;
			this.img = img.copy();
		}

		public void setPosition(int x, int y) 
		{
			this.x = x;
			this.y = y;
		}
		
		public float getX()
		{
			return x;
		}

		public float getY()
		{
			return y;
		}
		
		public void hit()
		{
			target.shipFight.hit(parentAttack);
			explosion = new Explosion(x,y,100,100);
			done = true;
		}
		
		public boolean isDone()
		{
			return done && explosion.animation.isStopped();
		}

		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
		{		
			if(screen_x != -1 || screen_y != -1)
			{
				g.drawImage(img, screen_x,screen_y, screen_x+screen_w, screen_y+screen_h, 0, 0, img.getWidth(), img.getHeight());	
			}	
		}

		public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
		{
			double distX = delta * (speed * Math.cos(getAngle()));
			double distY = delta * (speed * Math.sin(getAngle()));
			
			if(Math.abs(getDistYFromTarget()) < Math.abs(distY) && Math.abs(getDistXFromTarget()) < Math.abs(distX)
					&& done == false)
			{
				hit();
			}else{
				x += distX;
				y += distY;
			}
			

			screenDims = FightState.getScreenDims(gc, new Rectangle(x,y,w,h), screenRect);

			screen_x = screenDims.getX();
			screen_y = screenDims.getY();
			screen_w = screenDims.getWidth();
			screen_h = screenDims.getHeight();
			
			float rotation = (float)(getAngle() * 180/Math.PI);
			
			img.setCenterOfRotation(screen_w/2, screen_h/2);
			img.setRotation(rotation);
			
			if(explosion != null)
			{
				explosion.update(gc, sbg, delta, screenRect);
			}
		}
		
		public double getDistYFromTarget()
		{
			return target.shipFight.getCenterY() - getY();
		}
		public double getDistXFromTarget()
		{
			return target.shipFight.getCenterX() - getX();
		}
		
		public double getAngle()
		{
			return Math.atan2(getDistYFromTarget(),getDistXFromTarget());
		}
	}
}
