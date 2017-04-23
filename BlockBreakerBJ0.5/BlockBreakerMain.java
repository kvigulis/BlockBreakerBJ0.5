
import java.lang.Math;

import org.lwjgl.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.util.*; 

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * This class launches the game and defines the main game mechanics
 */
public class BlockBreakerMain {

    // The window handle
    private long window;
    private int windowWidth = 1000;
    private int windowHeight = 768;
    
    private static float deltaCursorX;
    private static float prevCursorX;
    private static float prevPrevCursorX;
    private static float prevPadPos;

    private float wallXSoftness = 0.92f;  // Zero is maximum softness.
    private float padSideSoftness = 0.8f;
    private float blockSideSoftness = 0.92f;

    private float padHeight = 0.02f;
    private static float padWidth = 0.5f;
    private float padHorizontalPosition = 0f;
    private float padVerticalPosition = -0.8f;
 

    private static int numberOfBalls = 3;
    private float ballRadius = 0.008f;
    private int ballsAddedCounter = 0;
    private int ballAddFrameCounter = 1;
    private int ballAddFrameCounterEnd = numberOfBalls*100;
    
    
    private float ballSpeedX;// = 0.5f;
    private float ballSpeedY; //= 0.5f;  

    
    // The block positions in Block[][] array are arranged from the centre. I.e. x = 0.0f & y = 0.0f. is the [0][0] element.
    private int rowsOfBlocks = 7;	// Use only odd numbers, please
    private int colsOfBlocks = 7;  // Use only odd numbers, please
    
    private float centreBlockY = 0.45f;  // Height of the central block
    
    private float blockWidth = 0.27f;
    private float blockHeight = 0.135f;
    
    private int blockHealthGreen = 1;
    private int blockHealthBlue = 2;
    private int blockHealthRed = 3;   
    private int blockHealthCentral = 6;
    
    private int gameOverFrameCounter = 1;    
    private int gameOverFrameCounterEnd = 4000;

    private String playerName;
    
    private static int lives;
    
    private static int level;
    
    private static int score;
    
    private boolean lostFirstFrame = true;

    private Random rand = new Random();
    private static int blocksDestroyed = 0;
    
    Block[][] blocks; 
    ColourOpenGL[][] blockColour;
    
    AudioFile ballTeleport = new AudioFile("ballTeleport.wav");
    AudioFile newLevel = new AudioFile("newLevel.wav");  
    AudioFile gameOver = new AudioFile("gameOver.au"); 
    
    

    /**
     * Constuctor
     * Runs the program
     * Run AskName when the game is over
     * Closes it afterwards
     *
     */

    public BlockBreakerMain(SettingGUI settingGUI)
    {
    	System.out.println("Hello LWJGL " + Version.getVersion() + "!");       

        blocksDestroyed = 0;
        level = 1;       
        

        init();
        loop(settingGUI); 
        AskName askName = new AskName();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }
    
    /**
     * Creates a window and sets it to be visible
     * <P>Sets the window to close when the escape key is pressed
     * <P>Checks the resolutio of the screen and centers the screen accordingly
     *
     */
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.

    	
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        // Create the window

        //int windowWidth = 1024;
        //int windowHeight = 768;
        window = glfwCreateWindow(windowWidth, windowHeight, "Block Beaker V0.3", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);        

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )   // When ESCAPE is pressed the window closes
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            });        

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    /**
     * Creates a pad with some parameters
     * <P>Creates and fills an array of blocks
     * <P>Creates and fills an array of balls
     * <P>Runs a loop, that redraws the objects and checks their position, until the user decides to quit
     *
     */
    private void loop(SettingGUI settingGUI) {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();        

        // Set the clear color
        glClearColor(0.1f, 0.1f, 0.2f, 0.5f);

        BallCollisionMechanics bcm = new BallCollisionMechanics();

        /*TrueTypeFont font;       

        InputStream inputStream	= ResourceLoader.getResourceAsStream("myfont.ttf");
        Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        font = new TrueTypeFont(awtFont, true);

        font.drawString(10,10,"Hello" , Color.yellow);*/

        Pad[] pads = new Pad[1];
        pads[0] = new Pad(padHeight, padWidth, padHorizontalPosition, padVerticalPosition, true);         
          
        Ball[] balls;        
        balls = new Ball[numberOfBalls]; 
        
        this.fillWindowWithBlocks();       
                
        TruetypeOversample teslaFontTTF = new TruetypeOversample(window, "TESLA.ttf");
        TruetypeOversample gameOverFontTTF = new TruetypeOversample(window, "GameOverFont.ttf");
        
        settingGUI.applySettings();

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the frame buffer  

            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);         

            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();   

            float currentCursorPos = (float)getCursorPosX(window);
            float currentDeltaCursorX = getDeltaCursorX(currentCursorPos);
            
            if ( lives == 0 ){
            	if(lostFirstFrame){
            		lostFirstFrame = false;
            		gameOver.playAudio(gameOver.getPath());
            		System.out.println("GameOver");
            	}
            	this.removeBallsAndResetBallAdder();
            	ballAddFrameCounter = numberOfBalls*100; // Invalidate the 'resetBallAdder'.            	
            	pads[0].setWidth(0);
            	gameOverFrameCounter++;
            	gameOverFontTTF.draw_world("GAME OVER", 405, 500, 5.0f, -2.0f, -2.0f);
            	gameOverFontTTF.draw_world("Press 'ESC' to save your score", 170, 600, 5.0f, 0.8f, -2.0f);
            	gameOverFontTTF.draw_world("Or Wait", 425, 650, 5.0f, 0.8f, -2.0f);
            	glLoadIdentity();
            	if(gameOverFrameCounter >= gameOverFrameCounterEnd){  // Used to add ball with time interval between additions            	
            		glfwSetWindowShouldClose(window, true); // When you run out of lives the window closes                	
                }	
            }
            else{
            	this.addBalls(balls, settingGUI, pads); // add balls in level 1.
            	
            	pads[0].draw(currentCursorPos, windowWidth, pads);              
 	
            	for(int b = 0; b<ballsAddedCounter; b++)
                {      
                    balls[b].draw();
                    bcm.updateBall(currentDeltaCursorX, prevPadPos, windowWidth, windowHeight, balls[b], pads, blocks, blockColour, wallXSoftness, padSideSoftness, blockSideSoftness);                    
                } 
            	
            	teslaFontTTF.draw_world(Float.toString(Math.round(pads[0].getWidth()/padWidth*10000)/100)+ "%",(int)(450+pads[0].getHorizontalPosition()*512), 720, 1.0f, 0.7f, 0.3f); // Pad Health           	
            	glLoadIdentity();  
            }                      

            for(int row = 0; row<this.rowsOfBlocks; row++)
            {            	            	            	
                for(int col = 0; col<this.colsOfBlocks; col++)
                {        	
                    blocks[row][col].draw(blockColour[row][col]);
                }
            }             

            for(int row = 0; row<blocks.length; row++)
            {
                for(int bl = 0; bl<blocks[0].length; bl++)
                {
                    if(blocks[row][bl].getHealth()<1)
                    {
                        if(!blocks[row][bl].getIsDestroyed())
                        {
                        	BlockBreakerMain.blocksDestroyed++;  // If block was not in the destroyed state before add +1 to blocks destroyed counter.
                                                       
                            
                            if(blocks[row][bl].getTransitionType() == "approachGreen"){
                            	score+=10;  
                            }
                            if(blocks[row][bl].getTransitionType() == "approachBlue"){
                            	score+=20;  
                            }
                            if(blocks[row][bl].getTransitionType() == "approachRed"){
                            	score+=50;  
                            }
                            if(blocks[row][bl].getTransitionType() == "approachViolet"){
                            	score+=500;  
                            }
                             
                        }
                        blocks[row][bl].setDestroyed(true);
                        blockColour[row][bl] = new ColourOpenGL(0.9f, 2.0f, 0.6f, 0.4f, 0.3f, 0.2f); // reset the colours
                    }    					
                }
            }            

            if(BlockBreakerMain.blocksDestroyed==blocks.length*blocks[0].length)   //When all the blocks are  destroyed. Do this...
            {            	
                for(int row = 0; row<blocks.length; row++)
                {
                    for(int bl = 0; bl<blocks[0].length; bl++)
                    {
                        blocks[row][bl].setDestroyed(false);        
                    }				
                }
                level++; 
                newLevel.playAudio(newLevel.getPath());
                numberOfBalls++;
                balls = new Ball[numberOfBalls];
                this.removeBallsAndResetBallAdder();                               
                rowsOfBlocks+=2;	// Use only odd numbers, please
                colsOfBlocks+=2;  // Use only odd numbers, please
                blockWidth = (1.96f-0.01f*(colsOfBlocks-1))/colsOfBlocks;
                blockHeight = (1.015f-0.01f*(rowsOfBlocks-1))/rowsOfBlocks;
                this.fillWindowWithBlocks();                
                pads[0].setWidth(padWidth);
                
                BlockBreakerMain.blocksDestroyed = 0;
            }           

            prevPadPos = getPrevPadPos(pads);

            teslaFontTTF.draw_world(Integer.toString(score), 450, 750, 1.0f, 0.7f, 0.3f);  // Score text render           
            teslaFontTTF.draw_world("Lives: " + lives, 850, 750, 1.0f, 0.7f, 0.3f);
            teslaFontTTF.draw_world("Level: " + level, 50, 750, 1.0f, 0.7f, 0.3f);           
            
            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
                        
        }          
    }

    /**
     * Retrieves current cursor/mouse position
     *
     * @param windowID Windows inside which the position is observed
     * @return Current cursor/mouse position
     */
    public static double getCursorPosX(long windowID) 
    {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowID, posX, null);
        return posX.get(0);    
    } 

    /**
     * Method getDeltaCursorX
     *
     * @param cursorPosX Current horizontal cursor/mouse position
     * @return Change in the horizontal cursor/mouse position
     */
    public static float getDeltaCursorX(float cursorPosX)
    {
        deltaCursorX = (2f*cursorPosX - prevPrevCursorX - prevCursorX)/2;
        prevCursorX = cursorPosX;
        prevPrevCursorX = prevCursorX;

        return deltaCursorX;
    }
    
    private void addBalls(Ball[] balls, SettingGUI settingGUI, Pad[] pads){
    	if(ballAddFrameCounter <= ballAddFrameCounterEnd){  // Used to add ball with time interval between additions            	
        	if(ballAddFrameCounter % 100 == 0){              		
                ballSpeedX = 2*settingGUI.getXSpeed()*rand.nextFloat()-settingGUI.getXSpeed();
                ballSpeedY = settingGUI.getYSpeed();
                balls[ballsAddedCounter] = new Ball(ballRadius, pads[0].getHorizontalPosition(), padVerticalPosition+0.02f, ballSpeedX, ballSpeedY);
                ballTeleport.playAudio(ballTeleport.getPath());
                ballsAddedCounter++;                    
        	}
        	ballAddFrameCounter++;
        }
    }
    
    private void removeBallsAndResetBallAdder(){   	     
    	       
    	ballsAddedCounter = 0; 
    	ballAddFrameCounter = 1;
    	ballAddFrameCounterEnd = numberOfBalls*100;
    }
    
    private void fillWindowWithBlocks(){
    	        
        blocks = new Block[rowsOfBlocks][colsOfBlocks];        
    	
        blockColour = new ColourOpenGL[rowsOfBlocks][colsOfBlocks]; 
        
        blocks[0][0] = new Block(this.blockWidth, this.blockHeight, 0f, centreBlockY, blockHealthCentral, "approachGold"); //central block
        blockColour[0][0] = new ColourOpenGL(1.0f, 0.9f, 0.5f, 0.2f, 0.2f, 0.0f);       		
       
        
        for(int bl = 1; bl<colsOfBlocks; bl += 2){          	// Fill central row.
        	float blockPosX = (bl+1)*(this.blockWidth+0.01f)/2f;            
            blocks[0][bl] = new Block(this.blockWidth, this.blockHeight, blockPosX, centreBlockY, blockHealthRed, "approachRed");   
            blocks[0][bl+1] = new Block(this.blockWidth, this.blockHeight, -blockPosX, centreBlockY, blockHealthRed, "approachRed");
            blockColour[0][bl] = new ColourOpenGL(1.2f, 0.6f, 0.6f, 0.4f, 0.3f, 0.2f);
            blockColour[0][bl+1] = new ColourOpenGL(1.2f, 0.6f, 0.6f, 0.4f, 0.3f, 0.2f);
        }
        
              
        for(int row = 1; row<this.rowsOfBlocks; row += 2){        	// Two rows are added in one iteration. +1 and -1.
        	
            float blockPosY = ((float)row+1)*(this.blockHeight+0.01f)/2f;         
            
            blocks[row][0] = new Block(this.blockWidth, this.blockHeight, 0, blockPosY+centreBlockY, blockHealthRed, "approachRed"); 
            blocks[row+1][0] = new Block(this.blockWidth, this.blockHeight, 0, -blockPosY+centreBlockY, blockHealthRed, "approachRed");
            blockColour[row][0] = new ColourOpenGL(1.2f, 0.6f, 0.6f, 0.4f, 0.3f, 0.2f);
            blockColour[row+1][0] = new ColourOpenGL(1.2f, 0.6f, 0.6f, 0.4f, 0.3f, 0.2f);
            
            for(int bl = 1; bl<colsOfBlocks; bl += 2){          	
            	float blockPosX = (bl+1)*(this.blockWidth+0.01f)/2;                
                blocks[row][bl] = new Block(this.blockWidth, this.blockHeight, blockPosX, blockPosY+centreBlockY, blockHealthBlue, "approachBlue");   
                blocks[row][bl+1] = new Block(this.blockWidth, this.blockHeight, -blockPosX, blockPosY+centreBlockY, blockHealthBlue, "approachBlue");
                blockColour[row][bl] = new ColourOpenGL(0.4f, 0.8f, 1.5f, 0.1f, 0.4f, 0.6f);
                blockColour[row][bl+1] = new ColourOpenGL(0.4f, 0.8f, 1.5f, 0.1f, 0.4f, 0.6f);
            }
            
            for(int bl = 1; bl<colsOfBlocks; bl += 2){          	
            	float blockPosX = (bl+1)*(this.blockWidth+0.01f)/2;                
                blocks[row+1][bl] = new Block(this.blockWidth, this.blockHeight, blockPosX, -blockPosY+centreBlockY, blockHealthGreen, "approachGreen");   
                blocks[row+1][bl+1] = new Block(this.blockWidth, this.blockHeight, -blockPosX, -blockPosY+centreBlockY, blockHealthGreen, "approachGreen"); 
                blockColour[row+1][bl] = new ColourOpenGL(0.9f, 2.0f, 0.6f, 0.2f, 0.4f, 0.2f);
                blockColour[row+1][bl+1] = new ColourOpenGL(0.9f, 2.0f, 0.6f, 0.2f, 0.4f, 0.2f);
            }                                   
            
        }    	
    }
    

    /**
     * Retrieves previuos pad position
     *
     * @param pads An array of pads
     * @return Previuos horizontal pad position
     */
    public static float getPrevPadPos(Pad[] pads)
    {
        return pads[0].getHorizontalPosition();
    }

    /**
     * Retrieves the number of points
     *
     * @return Points
     */
    public static int getPoints()
    {
        return score;
    }

    /**
     * Retrieves the player's name
     *
     * @return Player's name
     */
    public String getName()
    {
        return playerName;
    }
    
    
    public static int getLives(){
    	return lives; 
    }
    
    public static void setLives(int l){
    	lives = l; 
    }
    
    public static void setNumberOfBalls(int n){
    	BlockBreakerMain.numberOfBalls = n; 
    }
    
    public static float getPadWidth(){
    	return padWidth;
    }

}
