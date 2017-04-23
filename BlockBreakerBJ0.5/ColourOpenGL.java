
public class ColourOpenGL {	

	// Used to store openGL graphical item colours with vertical colour gradient.
	
	private float rT;
    private float gT;
    private float bT;
    private float rB;
    private float gB;
    private float bB;
    
    public ColourOpenGL(float rt, float gt, float bt, float rb, float gb, float bb){
    	        
        this.rT = rt;
        this.gT = gt;
        this.bT = bt;
        this.rB = rb;
        this.gB = gb;
        this.bB = bb;
    }
	
	public void setRT(float rt){
    	this.rT = rt;
    }
	public void setBT(float bt){
		this.bT = bt;
	}
	public void setGT(float gt){
		this.gT = gt;
	}
	public void setRB(float rb){
		this.rB = rb;
	}
	public void setBB(float bb){
		this.bB = bb;
	}
	public void setGB(float gb){
		this.gB = gb;
	}
	
    public float getRT(){
    	return rT;
    }
	public float getBT(){
		return bT;
	}
	public float getGT(){
		return gT;
	}
	public float getRB(){
		return rB;
	}
	public float getBB(){
		return bB;
	}
	public float getGB(){
		return gB;
	}
		
}