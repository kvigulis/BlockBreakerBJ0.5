
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
/**
 * This class is to define the pad and then draw it
 * 
 */
public class Pad {

    private float height;//height of the pad
    private float width;//width of the pad
    private float horizontalPos;//horizontal position of the pad
    private float verticalPos;//vertical position of the pad
    private float prevCursorX;//previuos horizontal cursor/mouse position
    private boolean wallInteraction;//interaction with side walls

    private float vertex1X;
    private float vertex2X;
    private float vertex3X;
    private float vertex4X;
    private float vertex1Y;
    private float vertex2Y;
    private float vertex3Y;
    private float vertex4Y;

    /**
     * Creates a pad with defined parameters
     * 
     * @param height Defines the height of the pad
     * @param width Defines the width of the pad
     * @param horizontalPos Defines the horizontal position of the pad
     * @param verticalPos Defines the vertical position of the pad
     * @param wallInteraction Defines a parameter, chich specifies if the pad touches the wall
     */
    public Pad(float height, float width, float horizontalPos, float verticalPos, boolean wallInteraction){
        this.height = height;
        this.width = width;
        this.verticalPos = verticalPos; 
        this.horizontalPos = horizontalPos;
        this.wallInteraction = wallInteraction;
    }

    /**
     * Draws the pad
     *
     * @param cursorPosX Cursor/mouse position
     * @param windowWidth Width of the window in which the pad will be drawn
     * @param pads Supplies an array of pads to be drawn
     */
    public void draw(float cursorPosX, float windowWidth, Pad pads[]){
        glBegin(GL_QUADS);
        float deltaCursorX = cursorPosX - prevCursorX;//calculates the change in horizontal position of the pad
        this.prevCursorX = cursorPosX;//sets the previuos cursor/mouse position to the current position

        if(this.wallInteraction)//if the pad interacts with a wall
        {
            if((this.horizontalPos+deltaCursorX/windowWidth)<-1f+this.width/2)//if pads horizontal position (centre of the pad) is less than the left border's coordinate + pad width 
            {
                this.horizontalPos = -1f + this.width/2 + 0.0001f;//update pads position to be slightly on the right of the left border
            }
            else if((this.horizontalPos+deltaCursorX/windowWidth)>1f-this.width/2)//if pads horizontal position (centre of the pad) is larger than the right border's coordinate - pad width 
            {
                this.horizontalPos = 1f - this.width/2 - 0.0001f;//update pads position to be slightly on the left of the right border
            }
            else{
                this.horizontalPos = this.horizontalPos + deltaCursorX/windowWidth;//else updates the position by the change in curosr/mouse position
            }
        }
        else
        {
            this.horizontalPos = pads[0].getHorizontalPosition();//else the position is just upadated
        }

        vertex1X = this.horizontalPos + this.width/2f;    // Top Right
        vertex1Y = this.verticalPos+this.height/2f;

        vertex2X = this.horizontalPos - this.width/2f;    // Top Left
        vertex2Y = this.verticalPos+this.height/2f;

        vertex3X = this.horizontalPos - this.width/2f;   // Bottom Left
        vertex3Y = this.verticalPos-this.height/2f;

        vertex4X = this.horizontalPos + this.width/2f;   // Bottom Right    
        vertex4Y = this.verticalPos-this.height/2f;

        glColor4f(1.0f, 0.8f, 0.2f, 1.0f);
        glVertex2f(vertex1X, vertex1Y);    // The vertexes are drawn in counterclockwise direction
        glVertex2f(vertex2X, vertex2Y);
        glColor4f(0.7f, 0.4f, 0.1f, 0.1f);
        glVertex2f(vertex3X, vertex3Y);
        glVertex2f(vertex4X, vertex4Y);
        glEnd();

    }

    /**
     * Retrieves horizontal position of the pad
     * 
     * @return Horizontal position of the pad
     */
    public float getHorizontalPosition(){
        return this.horizontalPos;
    }

    /**
     * Retrieves horizontal position of the pad
     *
     * @return Horizontal position of the pad
     */
    public float getVerticalPosition(){
        return this.verticalPos;
    }

    /**
     * Retrieves height of the pad
     *
     * @return Height of the pad
     */
    public float getHeight(){
        return this.height;
    }

    /**
     * Retrieves width of the pad
     *
     * @return Width of the pad
     */
    public float getWidth(){
        return this.width;
    }

    /**
     * Sets width of the pad
     *
     * @param w Width of the pad
     */
    public void setWidth(float w){
        this.width = w;
    }

}

