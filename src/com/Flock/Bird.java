package com.Flock;

import java.awt.*;

class Bird
{
    protected static Dimension map = new Dimension(700,500);
    protected Point location = new Point(0,0);	
    protected static int detectDistance;
    protected static int separateDistance;
    protected Color color;
    private int currentTheta;
    private double currentSpeed;

    Bird(int x, int y, int theta, Color birdColor) {
        location.x = x;
        location.y = y;
        currentTheta = theta;
        color = birdColor;
    }
    
    Bird(Color birdColor) {
        this((int)(Math.random() * map.width), (int)(Math.random() * map.height), (int)(Math.random() * 360), birdColor);
    }
    
    static void setMapSize(Dimension newSize) {
        map = newSize;
    }
    
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillArc(location.x + 5, location.y + 5, 10, 10, 0, 360);
    }
    
    public void move(int flockTheta) {   	
        currentTheta = flockTheta;       
        location.x += (int)(currentSpeed * Math.cos(currentTheta * Math.PI/180));
        location.y -= (int)(currentSpeed * Math.sin(currentTheta * Math.PI/180));
        
        while (location.x  > map.width - 30) {          
        	 currentTheta += Math.random() * 360; 
        	 location.x += (int)(currentSpeed * Math.cos(currentTheta * Math.PI/180));        	
        }   
        
        while (location.x < 0) {          
       	 currentTheta += Math.random() * 360; 
       	 location.x += (int)(currentSpeed * Math.cos(currentTheta * Math.PI/180));
       }   
        
        while (location.y  > map.height - 20) {          
       	 currentTheta += Math.random() * 360; 
       	 location.y -= (int)(currentSpeed * Math.sin(currentTheta * Math.PI/180));
       }   
        
        while (location.y  < 0) {          
       	 currentTheta += Math.random() * 360; 
       	 location.y -= (int)(currentSpeed * Math.sin(currentTheta * Math.PI/180));
       }   
    }
    
    
    public int getDistance(Point p) {
        int dX = p.x - location.x;
        int dY = p.y - location.y;
        return (int)Math.sqrt( Math.pow( dX, 2 ) + Math.pow( dY, 2 ));
    }
 
    public int getTheta() {
        return currentTheta;
    }
 
    public Point getLocation() {
        return location;
    }
 
    public void setSpeed(int speed) {
        currentSpeed = speed;
    }
    
    public Color getColor() {
        return color;
    }

    
}