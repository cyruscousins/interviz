package math;

public class Polynomial implements F1Var{
	float[] coefficients;
	public Polynomial(float[] coefs){
		this.coefficients = coefs;
	}
	
	public float val(float f){
		float v = coefficients[0];
		for(int i = 0; i < coefficients.length; i++){
			float t = f;
			for(int j = 1; j < i; j++){
				t *= f;
			}
			v += t * coefficients[i];
		}
		return v;
	}
}
