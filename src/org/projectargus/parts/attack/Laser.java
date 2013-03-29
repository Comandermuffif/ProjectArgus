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

public class Laser extends Attack
{
	ArrayList<LaserPhase> laserPhases = new ArrayList<LaserPhase>();

	Image img;

	int amount = 2;
	int fired = 0;

	int currentInterval = 0;
	int interval = 500;

	public Laser() 
	{
		super();

		try {
			img = new Image("res/img/laser.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void fire(PartSlot slot, ArrayList<Ship> targets)
	{		
		laserPhases.clear();
		this.slot = slot;
		fired = 0;
		
		this.targets = targets;
		state = State.FIRING;
	}
	

	@Override
	public void firingRender(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{
		for(int i = 0;i < laserPhases.size();i++)
		{
			laserPhases.get(i).render(gc, sbg, g);
		}		
	}
	
	@Override
	public void firingUpdate(GameContainer gc, StateBasedGame sbg, int delta,Rectangle screenRect)
	{
		if(fired < amount)
		{
			currentInterval += delta;
			
			if(currentInterval > interval)
			{
				currentInterval = 0;
				laserPhases.add(new LaserPhase());
				fired++;
			}
		}			
		
		for(int i = 0; i < laserPhases.size();i++)
		{
			laserPhases.get(i).update(gc, sbg, delta, screenRect);
		}		
	}

	public boolean isDone()
	{
		for(int i = 0;i<laserPhases.size();i++)
		{
			if(laserPhases.get(i).isDone() == false)
			{
				return false;
			}
		}

		return fired >= amount;
	}

	public class LaserPhase
	{
		ArrayList<LaserStrike> lasers = new ArrayList<LaserStrike>();

		public LaserPhase()
		{
			for(int j = 0;j<targets.size();j++)
			{
				Ship target = targets.get(j);
				lasers.add(new LaserStrike(target));
			}
		}

		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
		{			
			for(int i = 0;i < lasers.size();i++)
			{
				lasers.get(i).render(gc, sbg, g);
			}
		}


		public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
		{
			for(int i = 0; i < lasers.size();i++)
			{
				lasers.get(i).update(gc, sbg, delta, screenRect);
			}
		}

		public boolean isDone()
		{
			for(int i = 0;i<lasers.size();i++)
			{
				if(lasers.get(i).isDone() == false)
				{
					return false;
				}
			}
			return true;
		}
	}


	public class LaserStrike
	{
		ArrayList<LaserPiece> pieces = new ArrayList<LaserPiece>();
		Explosion explosion;

		public LaserStrike(Ship target)
		{
			int dist = (int) Math.sqrt(Math.pow(target.shipFight.getCenterX() - slot.getX(), 2) + Math.pow(target.shipFight.getCenterY() - slot.getY(), 2));
			double angle = Math.atan2(target.shipFight.getCenterY() - slot.getY(),target.shipFight.getCenterX() - slot.getX());

			int w = 50;

			for(int i = 0;i*w < dist;i++)
			{
				float x = (float) ((i*w) * Math.cos(angle)+ slot.getX());
				float y = (float) ((i*w) * Math.sin(angle) + slot.getY());

				if(dist < (i+1)*w)
				{
					int tempW = dist - (i)*w;
					int tempH = w/2;

					pieces.add(new LaserPiece(x,y,tempW,tempH,angle,img));
				}
				else{
					pieces.add(new LaserPiece(x,y,w,w/2,angle,img));
				}			
			}

			explosion = new Explosion(target.shipFight.getCenterX(),  target.shipFight.getCenterY(), 100,100);
			
			target.shipFight.hit(parentAttack);
		}



		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
		{			
			for(int i = 0;i < pieces.size();i++)
			{
				pieces.get(i).render(gc, sbg, g);
			}

			if(explosion != null)
			{
				explosion.render(gc, sbg, g);
			}
		}

		public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
		{
			if(isDone())
			{
				pieces.clear();
			}

			for(int i = 0; i < pieces.size();i++)
			{
				pieces.get(i).update(gc, sbg, delta, screenRect);
			}

			if(explosion != null)
			{
				explosion.update(gc, sbg, delta, screenRect);
			}
		}

		public boolean isDone()
		{	
			for(int i = 0;i<pieces.size();i++)
			{
				if(pieces.get(i).isDone() == false)
				{
					return false;
				}
			}

			return explosion.animation.isStopped();
		}
	}

	public class LaserPiece
	{
		float x;
		float y;
		float w;
		float h;

		double angle;

		Image img;

		Rectangle screenDims = new Rectangle(-1,-1,0,0);

		public LaserPiece(float x, float y,float w,float h,double angle,Image img)
		{
			this.x = x;
			this.y = y;	
			this.w = w;
			this.h = h;
			this.angle = angle;

			this.img = img.copy();		
		}

		public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
		{			
			if(screenDims.getX() != -1 || screenDims.getY() != -1)
			{
				img.draw(screenDims.getX(), screenDims.getY(), screenDims.getWidth(), screenDims.getHeight());
			}
		}

		public boolean isDone()
		{
			return h <= 0;
		}

		public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
		{
			h -= .08 * delta;

			if(h < 0)
			{
				//lasers.clear();
				h = 0;
			}

			screenDims = FightState.getScreenDims(gc, new Rectangle(x,y,w,h), screenRect);

			img.setCenterOfRotation(screenDims.getWidth()/2,screenDims.getHeight()/2);
			img.setRotation((float) Math.toDegrees(angle));	
		}
	}
}
