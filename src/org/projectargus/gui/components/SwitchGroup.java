package org.projectargus.gui.components;

import java.util.ArrayList;

public class SwitchGroup {

	ArrayList<Switch> switches = new ArrayList<Switch>();
	
	public void addSwitch(Switch switch1) 
	{
		if(switches.contains(switch1) == false)
		{
			switches.add(switch1);
			if(this.equals(switch1.getGroup()) == false)
			{
				switch1.addToGroup(this);
			}
		}
	}

	public void setSelected(Switch switch1) 
	{
		for(int i = 0;i<switches.size();i++)
		{
			switches.get(i).setSelected(false);
		}
		
		switch1.setSelected(true);
	}
}
