package org.projectargus.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Button 
{
	//Rectangle which the button is contained in
	Rectangle rect;
	
	//Three images for each of the states
	Image imgOut;
	Image imgOver;
	Image imgPressed;
	
	//State holder variable
	State state = State.OUT;
	
	//Types of state
	private enum State
	{
		OUT,OVER,PRESSED
	}

	/**
	 * @param x The left position of the button
	 * @param y The top position of the button
	 * @param width the width of the button
	 * @param height the height of the button
	 */
	public Button(int x,int y, int width, int height)
	{		
		rect = new Rectangle(x,y,width,height);	
		
		try {
			imgOut = new Image("res/img/buttonOut.png");
			imgOver = new Image("res/img/buttonOver.png");
			imgPressed = new Image("res/img/buttonPressed.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();		
		//Checks to see if the mouse is within the button
		if(rect.contains(input.getMouseX(), input.getMouseY()))
		{
			if(input.isMouseButtonDown(0))
			{
				state = State.PRESSED;
			}else{
				state = State.OVER;
			}
			
			//If the mouse clicks the button
			if(input.isMousePressed(0))
			{
				onClick();
			}
		}
		else{
			state = State.OUT;
		}		
	}

	public void render(Graphics g)
	{
		//Selects the correct graphic to draw for the current state
		switch(state)
		{
		case OUT:
			imgOut.draw(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
			break;
		case OVER:
			imgOver.draw(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
			break;
		case PRESSED:
			imgPressed.draw(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
			break;
		};		
	}
	
	public void onClick()
	{
		System.out.println("Button Pressed!");
	}
}
