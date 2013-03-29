package org.projectargus.states;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.projectargus.Main;
import org.projectargus.data.GameData;
import org.projectargus.gui.FightAttackMenu;
import org.projectargus.objects.Ship;

public class FightState extends BasicGameState 
{
	//Will be assigned actual value on construct from main class
	int stateID = -1;

	//A singleton class which stores all data across all classes
	GameData data = GameData.getInstance();

	float zoom = 95;
	float maxZoom = 100;

	float startMouseX = -1;
	float startMouseY = -1;

	int x = 800;
	int y = 600;

	Rectangle screenRect = new Rectangle(0,0,0,0);

	Rectangle selectedRectange = new Rectangle(0,0,0,0);	

	FightAttackMenu attackMenu = new FightAttackMenu();

	Rectangle fireBtn = new Rectangle(550,10,50,20);
	Rectangle cancelBtn = new Rectangle(625,10,70,20);



	public States state = States.FIGHT;	
	public FightStates fightState = FightStates.SELECT_SHIP;	
	public EndStates endState = EndStates.SUMMARY;	


	Ship selectedShip = null;
	ArrayList<Ship> targets = new ArrayList<Ship>();
	ArrayList<Rectangle> targetsPoints = new ArrayList<Rectangle>();
	




	public enum States
	{
		START,FIGHT,END
	}

	public enum FightStates
	{
		SELECT_ATTACK,SELECT_TARGET,FIRING,DONE, SELECT_SHIP, ENEMY_ATTACK
	}
	
	public enum EndStates
	{
		SELECT_RESOURCES,SELECT_SHIPS,SUMMARY
	}

	public FightState( int stateID ) 
	{
		this.stateID = stateID;
	}

	@Override
	public int getID() 
	{
		return stateID;
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Entering Fight");
	}

	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Leaving Fight");
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		System.out.println("Initializing Fight");
	}

	//Draw everything here
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setColor(Color.green);
		g.drawString(getState() + " - " + getFightState(), 100, 10);

		switch(getState())
		{
		case FIGHT:

			//Render each fleet
			if(data.enemyFightFleet.getShips().contains(selectedShip))
			{
				data.playerFightFleet.fightRender(gc, sbg, g);
				data.enemyFightFleet.fightRender(gc, sbg, g);
			}else{
				data.enemyFightFleet.fightRender(gc, sbg, g);
				data.playerFightFleet.fightRender(gc, sbg, g);
			}

			//Draw for the selectedShip
			if(selectedShip != null)
			{
				g.setColor(new Color(255,255,255));
				g.fillOval(selectedRectange.getX(),selectedRectange.getY(),selectedRectange.getWidth()/4,selectedRectange.getHeight()/4);
			}

			switch(getFightState())
			{
			case SELECT_SHIP:
				break;
			case SELECT_ATTACK:
				attackMenu.render(gc, sbg, g);
				break;
			case SELECT_TARGET:
				if(targets.size() > 0)
				{
					g.setColor(Color.gray);
					g.fill(fireBtn);
					g.setColor(Color.white);
					g.drawString("Fire", fireBtn.getX() + 5,fireBtn.getY());
				}
				g.setColor(Color.gray);
				g.fill(cancelBtn);
				g.setColor(Color.white);
				g.drawString("Cancel", cancelBtn.getX() + 5,cancelBtn.getY());
				g.drawString("Click on ships to select as targets.", 500,40);
				
				
				//Draws all target points.
				g.setColor(new Color(0,255,50));
				for(int i = 0;i<targetsPoints.size();i++)
				{
					g.fillOval((int)targetsPoints.get(i).getX(),(int)targetsPoints.get(i).getY(),(int)targetsPoints.get(i).getWidth(),(int)targetsPoints.get(i).getHeight());
				}
				
				

				break;
			case ENEMY_ATTACK:

				break;
			case DONE:

				selectedShip = null;
				fightState = FightStates.SELECT_SHIP;

				break;
			case FIRING:
				break;
			default:
				break;

			}
			break;
		case END:

			switch(endState)
			{
			case SELECT_RESOURCES:
				
				if(data.playerFightFleet.getLivingShips().size() == 0)
				{
					g.drawString("You lost and therefore cant take any resources :p", 455, 320);
				}
				else{
					g.drawString("Select the plunder you would like to take from the enemy", 450, 320);
				}
				break;
			case SELECT_SHIPS:
				if(data.playerFightFleet.getLivingShips().size() == 0)
				{
					g.drawString("You cannot select ships because you lost", 505, 320);
				}
				else{
					g.drawString("Select ships you want to take from the enemy", 500, 320);
				}
				break;
			case SUMMARY:
				g.drawString("Fight Over!", 600, 300);

				if(data.playerFightFleet.getCapableShips().size() == 0)
				{
					g.drawString("Summary - You just lost the fight!", 525, 320);
				}
				else{
					g.drawString("Summary - You just won the fight!", 525, 320);
				}
				break;
			default:
				break;
			
			}



			break;
		case START:
			break;
		default:
			break;
		}

	}

	//Do all calculations here
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException 
	{
		Input input = gc.getInput();		

		int mouse_x = input.getMouseX();
		int mouse_y = input.getMouseY();

		if(input.isKeyPressed(Input.KEY_SPACE))
		{
			sbg.enterState(Main.MAIN_MENU_STATE);
		}


		switch(getState())
		{
		case FIGHT:

			//Check for changes in state here...



			switch(getFightState())
			{
			case SELECT_SHIP:

				if(isFightOver())
				{
					state = States.END;
					break;
				}


				selectedShip = getNextTurnShip();

				targets.clear();
				targetsPoints.clear();

				if(data.playerFightFleet.getShips().contains(selectedShip))
				{
					fightState = FightStates.SELECT_ATTACK;							
					//selectedShip.shipFight.setSelected(true);
					attackMenu.setBox(selectedShip);

				}else{
					System.out.println("Enemy Attack!");
					fightState = FightStates.ENEMY_ATTACK;
				}

				break;
			case SELECT_ATTACK:
				if(input.isMousePressed(0))
				{
					int attackNum = attackMenu.checkClick(mouse_x, mouse_y);
					if(attackNum != -1)
					{
						selectedShip.shipFight.selectAttack(attackNum);
						fightState = FightStates.SELECT_TARGET;
					}
				}
				break;
			case SELECT_TARGET:
				if(input.isMousePressed(0))
				{
					if(selectedShip != null)
					{
						Ship tempShip = data.playerFightFleet.fleetFight.checkClicked(mouse_x, mouse_y);
						if(tempShip == null)
						{
							tempShip = data.enemyFightFleet.fleetFight.checkClicked(mouse_x, mouse_y);
						}
						if(tempShip != null)
						{
							targets.add(tempShip);
						}
					}

					if(fireBtn.contains(mouse_x, mouse_y) 
							&& targets.size() > 0)
					{
						System.out.println("Firing!");
						fightState = FightStates.FIRING;
						selectedShip.shipFight.fire(targets);
					}
					else if(cancelBtn.contains(mouse_x, mouse_y))
					{
						System.out.println("Cancel!");
						targets.clear();
						fightState = FightStates.SELECT_ATTACK;
					}
				}
				
				targetsPoints.clear();
				for(int i = 0;i<targets.size();i++)
				{
					Rectangle screenPos = getScreenDims(gc,new Rectangle(targets.get(i).shipFight.getX(),targets.get(i).shipFight.getY(),20,20),screenRect);
					targetsPoints.add(screenPos);
				}
				
				
				break;
			case FIRING:
				if(selectedShip.shipFight.isDoneFiring())
				{

					fightState = FightStates.SELECT_SHIP;

				}
				break;
			case ENEMY_ATTACK:

				selectedShip.shipFight.selectAttack((int) (selectedShip.getInventory().getParts().getAllWeaponAttacks().size() * Math.random()));
				for(int i = 0;i<data.playerFightFleet.getLivingShips().size();i++)
				{
					if(Math.random() < .75)
					{
						targets.add((Ship)(data.playerFightFleet.getLivingShips().get(i)));
					}
				}

				if(data.playerFightFleet.getShips().size() > 0)
				{
					if(targets.size() == 0)
					{
						targets.add((Ship)(data.playerFightFleet.getLivingShips().get((int) (Math.random()*
								data.playerFightFleet.getLivingShips().size()))));
					}

					selectedShip.shipFight.fire(targets);
				}

				fightState = FightStates.FIRING;

				break;
			case DONE:			
				break;
			default:
				break;
			}


			int size = data.fightScreenSize;

			//Find the rectangle of fight scene to display 
			int tempX = x - (int)((float)size/2 * getZoomRatio());
			int tempY = y - (int)((float)size/2 * getZoomRatio()) * gc.getHeight()/gc.getWidth();
			int tempW = (int)((float)size * getZoomRatio());
			int tempH = ((int)((float)size * getZoomRatio())) * gc.getHeight()/gc.getWidth(); //Ratio creates rectangle rather than square

			//Create the SolarSystem rectangle that the user can see		
			screenRect = new Rectangle(tempX,tempY,tempW,tempH);

			if(selectedShip != null)
			{
				selectedRectange = getScreenDims(gc,new Rectangle(selectedShip.shipFight.getX(),selectedShip.shipFight.getY(),selectedShip.shipFight.getWidth(),selectedShip.shipFight.getHeight()),screenRect);
			}

			if(input.isMouseButtonDown(0))
			{
				if(startMouseX != -1)
				{
					float diffX = startMouseX - mouse_x;
					float diffY = startMouseY - mouse_y;

					x += diffX/(float)gc.getWidth() * size * getZoomRatio();
					y += (diffY/(float)gc.getHeight() * size * getZoomRatio())*gc.getHeight()/gc.getWidth();

					if(x > size)
					{
						x = size;
					}
					if(y > size)
					{
						y = size;
					}
					if(x < 0)
					{
						x = 0;
					}
					if(y < 0)
					{
						y = 0;
					}
				}

				startMouseX = mouse_x;
				startMouseY = mouse_y;		
			}
			else
			{
				startMouseX = -1;
				startMouseY = -1;
			}

			data.enemyFightFleet.fightUpdate(gc, sbg, delta, screenRect);
			data.playerFightFleet.fightUpdate(gc, sbg, delta, screenRect);


			break;
		case END:		
			break;
		case START:
			break;
		default:
			break;
		}
	}

	private Ship getNextTurnShip() 
	{
		Ship tempShip;

		if(Math.random() > .5)
		{
			tempShip =  data.playerFightFleet.getCapableShips().get(
					(int) (data.playerFightFleet.getCapableShips().size() * Math.random()));
		}else{
			tempShip =  data.enemyFightFleet.getCapableShips().get(
					(int) (data.enemyFightFleet.getCapableShips().size() * Math.random()));
		}

		return tempShip;
	}

	private boolean isFightOver() 
	{
		/*System.out.println((data.enemyFightFleet.getShips().size() == 0) + " : " + 
(data.playerFightFleet.getShips().size() == 0) + " : " + data.enemyFightFleet.fleetFight.isDone()
+ " : " + data.playerFightFleet.fleetFight.isDone());*/

		return (data.enemyFightFleet.getCapableShips().size() == 0 || 
				data.playerFightFleet.getCapableShips().size() == 0);
		//&& (data.enemyFightFleet.fleetFight.isDone() && data.playerFightFleet.fleetFight.isDone());
	}


	public States getState()
	{
		return state;
	}

	public FightStates getFightState()
	{
		return fightState;
	}


	//Controls zoom in and out
	public void mouseWheelMoved(int change) 
	{				
		zoom += (float)(change)/(float)30;

		if(zoom < 0)
		{
			zoom = 0;
		}
		if(zoom > 99.5)
		{
			zoom = (float) 99.5;
		}
	}

	//Zoom
	public float getZoomRatio()
	{
		return 2*(1 - (float)zoom/maxZoom);
	}

	/**
	 * Sets the screen position and size of the fleet 
	 * @param gc The GameContainer passed through the update method
	 * @param screenRect The visible coordinates
	 */
	public static Rectangle getScreenDims(GameContainer gc,Rectangle dims, Rectangle screenRect)
	{
		float viewWidth = ((float)screenRect.getWidth());
		float viewHeight = ((float)screenRect.getHeight());

		float x = dims.getX();
		float y = dims.getY();
		float w = dims.getWidth();
		float h = dims.getHeight();

		float screen_w = w/viewWidth * gc.getWidth();
		float screen_h = h/viewHeight * gc.getHeight();
		float screen_x = -1;
		float screen_y = -1;


		if(screenRect.contains(x,y) || screenRect.contains(x,y + h)
				|| screenRect.contains(x+w,y)
				|| screenRect.contains(x+w,y + h))
		{
			screen_x = gc.getWidth() * ((x - screenRect.getX()) / screenRect.getWidth());
			screen_y = gc.getHeight() * ((y - screenRect.getY()) / screenRect.getHeight());
		}

		return new Rectangle(screen_x,screen_y,screen_w,screen_h);
	}
}