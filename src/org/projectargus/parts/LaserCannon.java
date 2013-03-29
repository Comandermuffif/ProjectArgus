package org.projectargus.parts;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.projectargus.parts.attack.Laser;

public class LaserCannon extends WeaponPart
{
	public LaserCannon() 
	{
		super("Laser Cannon", "Launches a laser",35,100,10f);
		attack = new Laser();
		
		try {
			img = new Image("res/img/laserCannon1.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
