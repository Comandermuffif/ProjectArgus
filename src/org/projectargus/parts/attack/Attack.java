package org.projectargus.parts.attack;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.data.GameData;
import org.projectargus.objects.Ship;
import org.projectargus.parts.PartSlot;
import org.projectargus.parts.WeaponPart;

public abstract class Attack 
{	
	GameData data = GameData.getInstance();

	PartSlot slot;
	
	public Attack parentAttack;
	
	State state = State.DONE;

	protected ArrayList<Ship> targets;

	enum State
	{
		FIRING,DONE
	}


	/**
	 * 
	 * @param x The starting x position of the attack
	 * @param y The starting y position of the attack
	 * @param angle The angle were the attack is aimed... Probably will change to just a ship.
	 */
	public Attack()
	{
		parentAttack = this;
		System.out.println("Attack Created");
	}

	public void fire(PartSlot slot,ArrayList<Ship> targets)
	{
		this.targets = targets;
		state = State.FIRING;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) 
	{			
		switch(getState())
		{
		case FIRING:
			firingRender(gc, sbg, g);
			break;
		case DONE:
			break;
		default:
			break;
		}
	}

	public abstract void firingRender(GameContainer gc, StateBasedGame sbg, Graphics g);

	public void update(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect) 
	{
		switch(getState())
		{
		case FIRING:
			firingUpdate(gc, sbg, delta,screenRect);
			break;
		case DONE:
			break;
		default:
			break;
		}
	}

	public abstract void firingUpdate(GameContainer gc, StateBasedGame sbg, int delta, Rectangle screenRect);
	
	public State getState()
	{
		if(state == State.FIRING)
		{
			if(isDone())
			{
				return State.DONE;
			}else{
				return State.FIRING;
			}
		}

		return state;
	}

	public abstract boolean isDone();

	public WeaponPart getWeapon() 
	{
		return (WeaponPart)(slot.getPart());
	}
}
