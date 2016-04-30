package ctf;

import apcs.Window;

public class CaptureTheFlag {
    
    static String me;       // My name
    static int team;        // Either 1 or 2
    static String[] people = { "keshav", "brendan", "alec", "tate", "nikhil", "rohan", "justin", "grant" };
    
    //My position
    static int x;
    static int y;
    
    public static void main(String[] args) {
        // Set up the mesh and game
        Window.mesh.join("104.236.128.169");
        Window.size(1000, 500);
        
        // Get this player's information
        me = Window.ask("What is your name?");
        team = Integer.parseInt(Window.ask("What team are you on?"));
        Window.mesh.write(me + "team", team);
        
        if (team == 1) {
        	x = 250;
        	y = 250;
        }
        else {
        	x = 750;
        	y = 250;
        }
        
        // Infinite drawing/moving loop
        while (true) {
            draw();
            moveMyself();
            checkForTag();
            checkForFlag();
            Window.frame();
        }
    }

    private static void checkForFlag() {
    	int flag = 1;
    	if (team == 1)
    		flag = 2;
    	
    	// If I'm close to this flag
    	int flagx = Window.mesh.read(flag + "x");
    	int flagy = Window.mesh.read(flag + "y");
    	
    	if (Math.abs(x - flagx) < 30 && Math.abs(y - flagy) < 30) {
    			Window.mesh.write(flag + "x", x);
    			Window.mesh.write(flag + "y", y);
    			
    			// If I reached my side
    			if (team == 1 && x < 500) {
    				Window.mesh.write(flag + "x", 950);
    				Window.mesh.write(flag + "y", 250);
    				
    				int score = Window.mesh.read(team + "score");
    				Window.mesh.write(team + "score", score ++);
    			}
    			
    			if (team == 2 && x > 500) {
                    // Reset the flag
                    Window.mesh.write(flag + "x", 50);
                    Window.mesh.write(flag + "y", 250);
                    
                    int score = Window.mesh.read(team + "score");
                    Window.mesh.write(team + "score", score + 1);
                }
    	}
	}

	private static void checkForTag() {
    	// If I'm on my safe side
    	if (team == 1 && x < 500)
    		return;
    	if (team == 2 && x > 500)
    		return;
    	//	If I'm on the opponent's side
    	for (String person : people) {
    		// If it's a different person on a different team
    		if (! person.equals(me) && 
    			Window.mesh.read(person + "team") != team) {
    			
    			//	Am I being tagged by that person?
    			int px = Window.mesh.read(person + "x");
    			int py = Window.mesh.read(person + "y");
    			
    			// Check if (x, y) is close to (px, py)
    			if (Math.abs(x - px) < 20 &&
    				Math.abs(y - py) < 20) {
    				
    				// Go back to my side
    				 if (team == 1) {
    			        	x = 250;
    			        	y = 250;
    			        }
    			        else {
    			        	x = 750;
    			        	y = 250;
    			        }
    				
    			}
    		}
    	}
	}

	private static void moveMyself() {
        // change my x and y position
    	if(Window.key.pressed("left"))
    		x -= 5;
    	if(Window.key.pressed("right"))
    		x += 5;
    	if(Window.key.pressed("up"))
    		y -= 5;
    	if(Window.key.pressed("down"))
    		y += 5;
    	
        // send that x and y position with mesh
    	Window.mesh.write(me + "x", x);
    	Window.mesh.write(me + "y", y);
        
    }

    private static void draw() {
        Window.out.background("green");
        Window.out.color("white");
        Window.out.line(500, 0, 500, 500);
        
        // Draw each flag
        Window.out.color("red");
        Window.out.square(Window.mesh.read("1x"), Window.mesh.read("1y"), 20);
        
        Window.out.color("blue");
        Window.out.square(Window.mesh.read("2x"), Window.mesh.read("2y"), 20);
        // For each person in the game
        for (String person : people) {
        	int x = Window.mesh.read(person + "x");
        	int y = Window.mesh.read(person + "y");
        	int team = Window.mesh.read(person + "team");
        	
        	if (team == 1)
        		Window.out.color("red");
        	if (team == 2)
        		Window.out.color("blue");
        	Window.out.circle(x, y, 10);
        	Window.out.print(person, x - 15, y + 20);
        }
        
    }

}
