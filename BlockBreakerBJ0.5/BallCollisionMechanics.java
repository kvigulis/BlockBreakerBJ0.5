/**
 * This class provides interaction between all of the object in the game.
 * 
 */

public class BallCollisionMechanics {

    /**
     * Empty BallCollisionMechanics Constructor for to escape static/non-static conflicts
     *
     */
	
	
	AudioFile greenBlock = new AudioFile("greenBlock.wav");
	AudioFile blueBlock = new AudioFile("blueBlock.wav");
	AudioFile redBlock = new AudioFile("redBlock.wav");
	AudioFile goldBlock = new AudioFile("goldBlock.wav");
	AudioFile padAndWallHit = new AudioFile("padAndWallHit.wav");	
	AudioFile ballLost = new AudioFile("ballLost.wav");
	AudioFile lifeLost = new AudioFile("lifeLost.wav");  
	
    public BallCollisionMechanics()
    {

    }

    /**
     * Upadtes ball's position and defines its interaction with pads, walls and blocks
     *
     * @param deltaCursorX Change in cursor/mouse position
     * @param prevPadPos Previous position of the pad
     * @param windowWidth Window width
     * @param windowHeight Window height
     * @param ball An array of balls
     * @param pads An array of pads
     * @param blocks An array of blocks
     * @param wallSoftness Softness of walls
     * @param padSideSoftness Softness of sides of pads
     * @param blockSideSoftness Softness of sides of blocks
     */
    public void updateBall(float deltaCursorX, float prevPadPos, float windowWidth, float windowHeight, Ball ball, Pad[] pads, Block[][] blocks, ColourOpenGL[][] blockColour, float wallSoftness, float padSideSoftness, float blockSideSoftness){

        ball.setPosX(ball.getPosX() + ball.getSpeedX()*10/windowWidth);
        ball.setPosY(ball.getPosY() + ball.getSpeedY()*10/windowHeight);
        if((ball.getPosX()>=1f && ball.getSpeedX()>0) || (ball.getPosX()<=-1f && ball.getSpeedX()<0))//if reaches the side walls, i.e. has positive velocity at right margin of the screen or has negative velocity at left margin of the screen
        {   		
            ball.setSpeedX(-ball.getSpeedX()*wallSoftness);//invert its horizontal velocity and multiply by softness
            padAndWallHit.playAudio(padAndWallHit.getPath());  
        }		

        if(ball.getPosY()>=1f && ball.getSpeedY()>0)//if ball reaches the top of the screen, i.e. its vertical position is larger or equal to the top of the screen and it has positive velocity
        {
            ball.setSpeedY(-ball.getSpeedY());//invert its vertical velocity, i.e. change the sign
            padAndWallHit.playAudio(padAndWallHit.getPath());  
        }
        if(ball.getPosY()<=-1f && ball.getSpeedY()<0)//if ball reaches the bottom of the screen, i.e. its vertical position is less or equal to the bottom of the screen and it has negative velocity 
        {	     
            ball.setSpeedY(-ball.getSpeedY());  //invert verical velocity of the ball, i.e. change sign
            float newPadWidth = pads[0].getWidth()-0.02f;//store a smaller pad width in a variable
            pads[0].setWidth(newPadWidth);//set the pad to have smaller width            
            ballLost.playAudio(ballLost.getPath());
            if(pads[0].getWidth()<=0f)//if pad's width is less or equal to zero
            {
                pads[0].setWidth(BlockBreakerMain.getPadWidth());		// Reset pad	
                BlockBreakerMain.setLives(BlockBreakerMain.getLives()-1);
                if(BlockBreakerMain.getLives()>0){
                	lifeLost.playAudio(lifeLost.getPath()); 
                }
                
                               
            }
            ball.setPosX(pads[0].getHorizontalPosition());
            ball.setPosY(-0.75f);
        }

        for(int p = 0; p<pads.length; p++)
        {
            if(ball.getPosY()<=pads[p].getVerticalPosition()+pads[p].getHeight()/2 + ball.getRadius() && 																// Hitting pad from sides
            ball.getPosY()>=pads[p].getVerticalPosition()-pads[p].getHeight()/2 - ball.getRadius() &&				
            (ball.getPrevPosX()>prevPadPos+pads[p].getWidth()/2 + ball.getRadius() && ball.getPosX()<=pads[p].getHorizontalPosition()+pads[p].getWidth()/2 + ball.getRadius()))
            {
                ball.setPosX(pads[p].getHorizontalPosition()+pads[p].getWidth()/2 + ball.getRadius() + 2.0f*deltaCursorX/windowWidth);
                ball.setSpeedX(-ball.getSpeedX()*padSideSoftness + deltaCursorX/40);
                padAndWallHit.playAudio(padAndWallHit.getPath());
            }
            else if(ball.getPosY()<=pads[p].getVerticalPosition()+pads[p].getHeight()/2 + ball.getRadius() && 
            ball.getPosY()>=pads[p].getVerticalPosition()-pads[p].getHeight()/2 - ball.getRadius() &&				
            (ball.getPrevPosX()<prevPadPos-pads[p].getWidth()/2 - ball.getRadius() && ball.getPosX()>=pads[p].getHorizontalPosition()-pads[p].getWidth()/2 - ball.getRadius())){
                ball.setPosX(pads[p].getHorizontalPosition()-pads[p].getWidth()/2 - ball.getRadius() + 2.0f*deltaCursorX/windowWidth);
                ball.setSpeedX(-ball.getSpeedX()*padSideSoftness + deltaCursorX/40);
                padAndWallHit.playAudio(padAndWallHit.getPath());
            }			
            else if(ball.getPosY()<=pads[p].getVerticalPosition()+pads[p].getHeight()/2 + ball.getRadius() && ball.getPosY()>=pads[p].getVerticalPosition()-pads[p].getHeight()/2 - ball.getRadius() &&		// Hitting pad from top or bottom
            ball.getPosX()<=pads[p].getHorizontalPosition()+pads[p].getWidth()/2 + ball.getRadius() && ball.getPosX()>=pads[p].getHorizontalPosition()-pads[p].getWidth()/2 - ball.getRadius()){
                ball.setSpeedY(-ball.getSpeedY());
                ball.setSpeedX(ball.getSpeedX() + deltaCursorX/40);
                padAndWallHit.playAudio(padAndWallHit.getPath());                
            }	
        }

        for(int row = 0; row<blocks.length; row++)
        {
            for(int bl = 0; bl<blocks[0].length; bl++)
            {	
                if(!blocks[row][bl].getIsDestroyed())
                {
                    if(ball.getPosY()<=blocks[row][bl].getVerticalPosition()+blocks[row][bl].getHeight()/2 &&  // Hitting blocks from sides
                    ball.getPosY()>=blocks[row][bl].getVerticalPosition()-blocks[row][bl].getHeight()/2 &&							  
                    ( (ball.getPrevPosX()>blocks[row][bl].getHorizontalPosition()+blocks[row][bl].getWidth()/2 && ball.getPosX()<=blocks[row][bl].getHorizontalPosition()+blocks[row][bl].getWidth()/2) || 
                        (ball.getPrevPosX()<blocks[row][bl].getHorizontalPosition()-blocks[row][bl].getWidth()/2 && ball.getPosX()>=blocks[row][bl].getHorizontalPosition()-blocks[row][bl].getWidth()/2) )
                    	
                    )
                    {
                        ball.setSpeedX(-ball.getSpeedX()*blockSideSoftness);
                        blocks[row][bl].doDamage(1, blockColour[row][bl]);
                        if(blocks[row][bl].getTransitionType() == "approachGreen"){
                        	greenBlock.playAudio(greenBlock.getPath());
                        }
                        else if(blocks[row][bl].getTransitionType() == "approachBlue"){
                        	blueBlock.playAudio(blueBlock.getPath());
                        }
                        else if(blocks[row][bl].getTransitionType() == "approachRed"){
                        	redBlock.playAudio(redBlock.getPath());                        	
                        }
                        else {
                        	goldBlock.playAudio(goldBlock.getPath());
                        }
                        
                    }
                    // Hitting blocks from top or bottom
                    else if(ball.getPosY()<=(blocks[row][bl].getVerticalPosition()+blocks[row][bl].getHeight()/2) && ball.getPosY()>=(blocks[row][bl].getVerticalPosition()-blocks[row][bl].getHeight()/2) &&
                    (ball.getPosX()<=blocks[row][bl].getHorizontalPosition()+blocks[row][bl].getWidth()/2) && (ball.getPosX()>=blocks[row][bl].getHorizontalPosition()-blocks[row][bl].getWidth()/2)){
                        ball.setSpeedY(-ball.getSpeedY());
                        blocks[row][bl].doDamage(1, blockColour[row][bl]);
                        if(blocks[row][bl].getTransitionType() == "approachGreen"){
                        	greenBlock.playAudio(greenBlock.getPath());
                        }
                        else if(blocks[row][bl].getTransitionType() == "approachBlue"){
                        	blueBlock.playAudio(blueBlock.getPath());
                        }
                        else if(blocks[row][bl].getTransitionType() == "approachRed"){
                        	redBlock.playAudio(redBlock.getPath());                        	
                        }
                        else {
                        	goldBlock.playAudio(goldBlock.getPath());
                        }
                    }	
                }							
            }
        }		

        ball.setPrevPosX(ball.getPosX());//set the ball's previuos horizontal position to the current one, i.e. upadate horizontal position	
    }
}