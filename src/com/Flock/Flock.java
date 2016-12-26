package com.Flock;

import java.util.Vector;
 

import java.awt.*;

class Flock {
    private Vector birds;
    static int separateDistance;
    static int detectDistance;    
    static Dimension map = new Dimension(700,500);  
    
    Flock()
    {
        birds = new Vector();
    }  
    
    static void setMapSize(Dimension newSize) {
        map = newSize;
    }
    
    public void addBird(Bird bird) {
        birds.addElement(bird);//将指定的组件添加到此向量的末尾，将其大小增加 1
    }   

    public void draw(Graphics g) {
        for (int i=0; i < birds.size(); i++) {
            Bird bird = (Bird)birds.elementAt(i);//返回指定索引处的组件
            bird.draw(g);
        }
    }   
  
    synchronized public void move() {
        int movingBird = 0;
    	while(movingBird < birds.size()){
    		Bird bird = (Bird)birds.elementAt(movingBird);
            bird.move(flockTheta(bird));
            movingBird ++;
    	}
    }

  
    synchronized void removeBird(Color color) {
        for (int i=0; i < birds.size(); i++) {
            Bird bird = (Bird)birds.elementAt(i);
            if (bird.getColor() == color) {
                birds.removeElementAt(i);
                break;
            }
        }
    }
    public void setBirdParameters( Color color, int speed ) {
        for (int i=0; i < birds.size(); i++) {
            Bird bird = (Bird)birds.elementAt(i);
            // if the color of the bird matches, then set the values
            if (bird.getColor() == color) {
                bird.setSpeed( speed);
                
            }
        }
    }
    
    private int flockTheta(Bird bird) {

        Point target = new Point(0, 0);
        int numBirds = 0;

        for (int i=0; i < birds.size(); i++) {
            Bird otherBird = (Bird)birds.elementAt(i);
            Point otherLocation =  otherBird.getLocation();
            int distance = bird.getDistance(otherLocation);
            
            if (!bird.equals(otherBird) && distance > 0 && distance <= detectDistance){
                if (bird.getColor().equals(otherBird.getColor())) {
                    Point alignment = new Point((int)(100 * Math.cos(otherBird.getTheta() * Math.PI/180)),
                    (int)(-100 * Math.sin(otherBird.getTheta() * Math.PI/180)));
                    alignment = normalisePoint(alignment, 100.0);
                   
                    double weight = 200.0;                    
                    if (distance < separateDistance) {
                        weight *= Math.pow(1 - (double) distance / separateDistance, 2);
                    }
                    else {
                        weight *= - Math.pow((double)( distance - separateDistance ) / ( detectDistance - separateDistance ), 2);
                    }
                    
                    Point attract = sumPoints(otherLocation, -1.0, bird.getLocation(), 1.0);
                    attract = normalisePoint(attract, weight);
                    
                    Point dist = sumPoints(alignment, 1.0, attract, 1.0);
                    dist = normalisePoint(dist, 100); 
                    target = sumPoints(target, 1.0, dist, 1.0);
                }                
               
                else {
                    Point dist = sumPoints(bird.getLocation(), 1.0, otherLocation, -1.0);
                    dist = normalisePoint(dist, 1000);
                    
                    double weight = Math.pow((1 - (double)distance/detectDistance), 2);
                    target = sumPoints(target, 1.0, dist, weight);
                }
                
                numBirds++;
            }
        }
        
        
        if (numBirds == 0) {
            return bird.getTheta();
        }
        else {
            target = sumPoints(bird.getLocation(), 1.0, target, 1/(double)numBirds);
            int targetTheta = (int)(180/Math.PI * Math.atan2(bird.getLocation().y - target.y, target.x - bird.getLocation().x));
            return (targetTheta + 360) % 360;
        }        
    }
    
    
    public Point sumPoints(Point p1, double w1, Point p2, double w2) {
        return new Point((int)(w1*p1.x + w2*p2.x), (int)(w1*p1.y + w2*p2.y));
    }
 
    
    public Point normalisePoint(Point p, double n) {
        if (Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2)) == 0.0) {
            return p;
        }
        else {
            double weight = n / Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
            return new Point((int)(p.x * weight), (int)(p.y * weight));
        }
    }
}
