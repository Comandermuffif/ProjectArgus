package org.projectargus.parts;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.projectargus.parts.attack.Missle;

public class MissileLauncher extends WeaponPart
{
	public MissileLauncher() 
	{
		super("Missile Launcher", "Launches missiles",20,10,10f);
		attack = new Missle();
		
		try {
			img = new Image("res/img/missileLauncher1.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
