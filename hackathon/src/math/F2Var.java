package math;

//Function of 2 variables
public abstract class F2Var {
    public static final float EPSILON = .001f;
    public static final float ONEOVER2EPSILON = (1 / (2 * EPSILON));
    
    public abstract float value(float x, float z);
    public float dydx(float x, float z){
        return (value(x + EPSILON, z) - value(x - EPSILON, z)) * ONEOVER2EPSILON;
    }
    public float dydz(float x, float z){
        return (value(x, z + EPSILON) - value(x, z - EPSILON)) * ONEOVER2EPSILON;
    }
}
