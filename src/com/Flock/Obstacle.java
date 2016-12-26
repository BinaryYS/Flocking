package com.Flock;

import java.awt.*;

class Obstacle extends Bird {
	
    Obstacle(int x, int y)
    {
        super(x, y, 0, Color.black); 
    }

    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillArc(location.x + 5, location.y + 5, 15, 15, 0, 360);
    }
}
