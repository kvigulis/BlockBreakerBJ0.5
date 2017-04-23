import static org.lwjgl.opengl.GL11.*;


/**
 * This class is to define the pad and then draw it. Also make it change colour when it is hit
 * 
 */
public class Block {

    private float height;
    private float width;
    private float horizontalPos;
    private float verticalPos;
    private int health;
    private int initialHealth;
    private boolean isDestroyed;    
    /**
     * private float representing the "red" part of top of the block
     */

    private float vertex1X;
    private float vertex1Y;
    private float vertex2X;
    private float vertex2Y;
    private float vertex3X;
    private float vertex3Y;
    private float vertex4X;	
    private float vertex4Y;	
    
    private String transitionType;

    /**
     * Constructs a block
     *
     * @param width Defines the width of the block
     * @param height Defines the height of the block
     * @param horizontalPos Defines the horizontal position of the block
     * @param verticalPos Defines the vertical position of the block
     * @param health Defines the "health" of the block
     */
    public Block(float width, float height, float horizontalPos, float verticalPos, int health, String transitionType){
        this.height = height;
        this.width = width;
        this.verticalPos = verticalPos;
        this.horizontalPos = horizontalPos;
        this.health = health;
        this.initialHealth = health;
        this.isDestroyed = false;
        this.transitionType = transitionType;
        
    }

    /**
     * Draws the block if its "health" is above 0
     *
     */
    public void draw(ColourOpenGL colour){
        glBegin(GL_QUADS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if(this.health>0){

            vertex1X = this.horizontalPos + this.width/2f;
            vertex1Y = this.verticalPos+this.height/2f;
            vertex2X = this.horizontalPos - this.width/2f;
            vertex2Y = this.verticalPos+this.height/2f;
            vertex3X = this.horizontalPos - this.width/2f;
            vertex3Y = this.verticalPos-this.height/2f;
            vertex4X = this.horizontalPos + this.width/2f;	
            vertex4Y = this.verticalPos-this.height/2f;

            glColor4f(colour.getRT(), colour.getGT(), colour.getBT(), 1.0f);
            glVertex2f(vertex1X, vertex1Y);    // The vertexes are drawn in counterclockwise direction
            glVertex2f(vertex2X, vertex2Y);
            glColor4f(colour.getRB(), colour.getGB(), colour.getBB(), 1.0f);
            glVertex2f(vertex3X, vertex3Y);
            glVertex2f(vertex4X, vertex4Y);		    
            //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	    

        }
        glEnd();		

    }

    /**
     * Sets the horizontal position of the block
     *
     * @param x Horizontal position
     */
    public void setX(float x){
        this.horizontalPos = x;
    }

    /**
     * Sets the horizontal position of the block
     *
     * @param y Horizontal position
     */
    public void setY(float y){
        this.verticalPos = y;
    }

    /**
     * Sets height of the block
     *
     * @param h Height
     */
    public void setHeight(float h){
        this.height = h;
    }

    /**
     * Sets width of the block
     *
     * @param w Width
     */
    public void setWidth(float w){
        this.width = w;
    }

    /**
     * Retrieves horizontal position of the block
     *
     * @return Horizontal position
     */
    public float getHorizontalPosition(){
        return this.horizontalPos;
    }

    /**
     * Retrieves vertical position of the block
     *
     * @return Vertical position
     */
    public float getVerticalPosition(){
        return this.verticalPos;
    }

    /**
     * Retrieves height of the block
     *
     * @return Height
     */
    public float getHeight(){
        return this.height;
    }

    /**
     * Retrieves width of the block
     *
     * @return Width
     */
    public float getWidth(){
        return this.width;
    }

    /**
     * Retrieves "health" of the block
     *
     * @return Health
     */
    public int getHealth(){
        return this.health;
    }

    /**
     * Sets "health" of the block
     *
     * @param Health
     */
    public void setHealth(int h){
        this.health = h;
    }

    /**
     * Does damage to the block, decreasing its health
     * <P>Changes the colour as the block gets damaged</P>
     *
     * @param d Damage
     */
    public void doDamage(int d, ColourOpenGL colour){
        this.health = this.health - d;
        
        
        if( this.transitionType == "approachRed"){
        	colour.setRT(colour.getRT()*(1f+1.0f/this.initialHealth));
            colour.setGT(colour.getGT()*(1f-0.9f/this.initialHealth));
            colour.setBT(colour.getBT()*(1f-0.6f/this.initialHealth));
            
            colour.setRB(colour.getRB()*(1f+0.4f/this.initialHealth));
            colour.setGB(colour.getGB()*(1f-1.2f/this.initialHealth));
            colour.setBB(colour.getBB()*(1f-1.5f/this.initialHealth));
             
        }
        
        if( this.transitionType == "approachBlue"){
        	colour.setRT(colour.getRT()*(1f-0.9f/this.initialHealth));
            colour.setGT(colour.getGT()*(1f-0.7f/this.initialHealth));
            colour.setBT(colour.getBT()*(1f+1.5f/this.initialHealth));
            
            colour.setRB(colour.getRB()*(1f-1.5f/this.initialHealth));
            colour.setGB(colour.getGB()*(1f-1.5f/this.initialHealth));
            colour.setBB(colour.getBB()*(1f+0.8f/this.initialHealth)); 
        }
        
        if( this.transitionType == "approachGreen"){
        	colour.setRT(colour.getRT()*(1f-1.0f/this.initialHealth));
            colour.setGT(colour.getGT()*(1f+1.0f/this.initialHealth));
            colour.setBT(colour.getBT()*(1f-1.0f/this.initialHealth));
            
            colour.setRB(colour.getRB()*(1f-1.5f/this.initialHealth));
            colour.setGB(colour.getGB()*(1f-0.1f/this.initialHealth));
            colour.setBB(colour.getBB()*(1f-1.5f/this.initialHealth)); 
        } 
        
        if( this.transitionType == "approachGold"){
        	colour.setRT(colour.getRT()*(1f+1.5f/this.initialHealth));
            colour.setGT(colour.getGT()*(1f+1.2f/this.initialHealth));
            colour.setBT(colour.getBT()*(1f-0.4f/this.initialHealth));
            
            colour.setRB(colour.getRB()*(1f+0.8f/this.initialHealth));
            colour.setGB(colour.getGB()*(1f+0.8f/this.initialHealth));
            colour.setBB(colour.getBB()*(1f-1.2f/this.initialHealth)); 
        }

               
    }

    /**
     * Checks if the block is destroyed
     *
     * @return Block's state
     */
    public boolean getIsDestroyed(){
        return this.isDestroyed;
    }

    /**
     * Destroys the block nad resets it's colour
     *
     * @param Block's state
     */
    public void setDestroyed(boolean b){
        this.isDestroyed = b;     

    }
    
    public int getInitialHealth(){
    	return initialHealth;
    }
    
    public String getTransitionType(){
    	return transitionType;
    }
    
   
	
	
}
