package math;

public class F2VarMapped extends F2Var{
	float[] data; //contains a border repeat.
	int length;
	int l1;
	float mapSize;
	//default constructor
	//	#construct float[] data int length float scale
	public F2VarMapped(float[] data, int length, float mapSize){
		this.data = data;
		this.length = length;
		l1 = length + 1;
		this.mapSize = length / mapSize;
	}
	public static F2VarMapped translateToSVF(float[] data, int length, float scale){
		int l1 = length + 1;
		float[] ndata = new float[l1 * l1];
		for(int y = 0; y < length; y++){
                    for(int x = 0; x < length; x++){
                            ndata[x + l1 * y] = data[x + length * y];
                    }
                }
		
		//repeat edges
		for(int i = 0; i < length; i++){
			ndata[i + l1 * length] = ndata[i];
			ndata[length + i * l1] = ndata[i * l1];
			//ndata[i + l1 * length] = ndata[i + l1 * (length - 1)];
			//ndata[length + i * l1] = ndata[length - 1 + i * l1];
		}
		ndata[l1 * l1 - 1] = ndata[0];
		
		return new F2VarMapped(ndata, length, scale);
	}
	public static F2VarMapped translateToSVF(float[][] data, int length, float scale){
		int l1 = length + 1;
		float[] ndata = new float[l1 * l1];
		for(int y = 0; y < length; y++)
		for(int x = 0; x < length; x++){
			ndata[x + l1 * y] = data[x][y];
		}
		return new F2VarMapped(ndata, length, scale);
	}
	public float value(float x, float y){
		x *= mapSize;
		y *= mapSize;
		
		if(x < 0){
			x -= length * ((int)x / length - 1);
		}
		if(y < 0){
			y -= length * ((int)y / length - 1);
		}
		
		int ix = (int) x;
		int iy = (int) y;
		
		
		float fx = x - ix;
		float fy = y - iy;
		
		if(ix >= length) ix %= length;
		if(iy >= length) iy %= length;
		
		//		System.out.println("x " + x + ", y " + y + ", ix " + ix + ", iy " + iy + ", fx " + fx + ", fy " + fy);
		//		System.out.println("Alen: " + data.length);
		
		iy *= l1; //sneaky.
		/*
		System.out.println(ix + iy);
		System.out.println(ix + 1 + iy);
		System.out.println(ix + iy + l1);
		System.out.println(ix + 1 + iy + l1);
		*/
		return 	(
		(data[ix + iy] * (1 - fx) + data[ix + 1 + iy] * fx) * (1 - fy) +
		(data[ix + iy + l1] * (1 - fx) + data[ix + 1 + iy + l1] * fx) * fy
		);
		
	}
	
	public static final java.text.DecimalFormat df = new java.text.DecimalFormat("000.00");
	public void printMap(){
		for(int y = 0; y < length; y++){
			String s = "";
			for(int x = 0; x < length; x++){
				s += df.format(value(x, y)) + "\t";
			}
			System.out.println(s);
		}
	}
	
	public void scaleOut(float scale){
		for(int i = 0; i < data.length; i++){
			data[i] *= scale;
		}
	}
	
    public void scaleIn(float scale){
            mapSize /= scale;
    }

}
