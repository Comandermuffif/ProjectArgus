package org.projectargus.parts.attack;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.states.FightState;

public class Explosion 
{
	Animation animation;

	float x;
	float y;
	float w;
	float h;

	private Rectangle screenDims;

	private float screen_x;
	private float screen_y;
	private float screen_w;
	private float screen_h;

	public Explosion(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;

		try {
			animation = new Animation(new SpriteSheet(new Image("res/img/explosion.png"),66,66),20);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		animation.start();
		animation.setLooping(false);
	}

	public Animation getAnimation()
	{
		return animation;
	}

	@SuppressWarnings("deprecation")
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
	{
		if(screenDims!= null)
		{
			if(screenDims.getX() !=-1 || screenDims.getY() != -1)
			{
				animation.draw(screen_x - screen_w/2,screen_y - screen_h/2,screen_w,screen_h);
			}else{
				animation.updateNoDraw();
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		screenDims = FightState.getScreenDims(gc, new Rectangle(x,y,w,h), screenRect);

		screen_x = screenDims.getX();
		screen_y = screenDims.getY();
		screen_w = screenDims.getWidth();
		screen_h = screenDims.getHeight();
	}
}
