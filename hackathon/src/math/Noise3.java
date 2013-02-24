package math;

//Static noise function generation utilities.

import java.util.Random;
public class Noise3{
	public static final float TWO_PI = (float)(Math.PI * 2);
	public static Random rand = new Random();
	
	public static F2Var noiseSVF(int width, float mapSize, float min, float max){
          
		return new F2VarMapped(genNoise2f(width, .92f, 30, 31, 1, 1, min, max), width, mapSize);
		//return new MappedSVF(genNoise2f(width, .93f, 30, 31, 1, 1, 0, 255), width, scale); //excellent noisey noise map
		//return new MappedSVF(genNoise2f(width, .7f, 11, 15, 1, 1, 0, 255), width, scale); //nice noisemap
//		return new MappedSVF2D(genNoise2f(width, .1f, 2, 31, 1, 1, min, max), width, scale); //testing striped map
	}
	
	public static float[] genNoise2f(int width, float ratio, int trigFunctions, float fWeight, float rWeight, int initialVectorComponentSize, float min, float max){
		int w1 = width + 1;
		//an array to hold the noise generated
		float[] noise = new float[w1 * w1];
		
		//the scalar coefficients of the sinusoids used
		float[] scalars = new float[trigFunctions];
		//First n terms of a geometric series: a1(1 - r ^ n) / (1 - r)
		float seriesSum = (1 - (float)Math.pow(ratio, trigFunctions)) / (1 - ratio);
		//range of function will be [-seriesSum, seriesSum]
		scalars[0] = ((float)fWeight) * .5f / seriesSum;
		//the sinusoids are calculated using these vectors, ie for vector <a, b>, the point (c, d) is calculated as sin(e * (a * c + b * d))
		int[] vectors = new int[trigFunctions * 2];
		
		//now calculate the vectors and the scalars
		
		//create 2 dimensional vectors, corresponding to the coefficients of each trig function, then make sure all vectors are nonzero
		for(int i = 0; i < trigFunctions; i++){
			int maxComponentSize = i + 1 + initialVectorComponentSize;
			vectors[i * 2] = rand.nextInt(maxComponentSize);
			//TODO circular vector selection
			//if(rand.nextBoolean()) vectors[i * 2] *= -1;
			vectors[i * 2 + 1] = rand.nextInt(maxComponentSize);
			if(vectors[i * 2] == 0 && vectors[i * 2 + 1] == 0)
			vectors[i * 2 + rand.nextInt(2)] = 1; //provide a unit vector if the 0 vector is given.
			if(i > 0){
				scalars[i] = scalars[i - 1] * ratio;
			}
		}
		
		float trigScalar = TWO_PI / width;
		
		//do it in this order for caching reasons.
		int yIndex = 0;
		float noiseValue = 0;
		for(int y = 0; y < width; y++){
			yIndex = y * w1;
			for(int x = 0; x < width; x++){
				//we now have an index and an x, y coordinate pair
				noiseValue = rand.nextFloat() * rWeight;
				for(int i = 0; i < trigFunctions; i++){
					int baseVector = i * 2;
					noiseValue += (float)(Math.sin((
					x * vectors[baseVector    ] +
					y * vectors[baseVector + 1]) *
					trigScalar) * scalars[i]);
				}
				noise[yIndex + x] = noiseValue;
			}
		}
		
		//and edge repeats
		int lastRow = w1 * width;
		for(int i = 0; i < width; i++){
			noise[width + i * w1] = noise[i * w1];
			noise[lastRow + i] = noise[i];
		}
		
		scale(noise, min, max);
		return noise;
	}
	public static void scale(float[] floats, float nmin, float nmax){
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for(int i = 0; i < floats.length; i++){
			if(floats[i] < min) min = floats[i];
			if(floats[i] > max) max = floats[i];
		}
		float scale = (nmax - nmin) / (max - min);
		for(int i = 0; i < floats.length; i++){ //TODO optimize down to 1 mult 1 add.
			floats[i] -= min;
			floats[i] *= scale;
                      floats[i] += nmin;
		}
	}
	
//	public static F3VarMapped noiseSVF3D(int width, float scale, float min, float max){
//		return new F3VarMapped(genNoise3f(width, .95f, 35, 31, 1, 1, min, max), width, scale);
//		//return new MappedSVF(genNoise2f(width, .93f, 30, 31, 1, 1, 0, 255), width, scale); //excellent noisey noise map
//		//return new MappedSVF(genNoise2f(width, .7f, 11, 15, 1, 1, 0, 255), width, scale); //nice noisemap
//		//return new MappedSVF(genNoise2f(width, .1f, 2, 31, 1, 1, 0, 255), width, scale); //testing striped map
//	}
	
	
	public static float[] genNoise3f(int width, float ratio, int trigFunctions, float fWeight, float rWeight, int initialVectorComponentSize, float min, float max){
		
		int dimensions = 3;
		
		int w1 = width + 1;
		//an array to hold the noise generated
		float[] noise = new float[w1 * w1 * w1];
		
		//the scalar coefficients of the sinusoids used
		float[] scalars = new float[trigFunctions];
		//First n terms of a geometric series: a1(1 - r ^ n) / (1 - r)
		float seriesSum = (1 - (float)Math.pow(ratio, trigFunctions)) / (1 - ratio);
		//range of function will be [-seriesSum, seriesSum]
		scalars[0] = ((float)fWeight) * .5f / seriesSum;
		//the sinusoids are calculated using these vectors, ie for vector <a, b>, the point (c, d) is calculated as sin(e * (a * c + b * d))
		int[] vectors = new int[trigFunctions * dimensions];
		
		//now calculate the vectors and the scalars
		
		//create 2 dimensional vectors, corresponding to the coefficients of each trig function, then make sure all vectors are nonzero
		for(int i = 0; i < trigFunctions; i++){
			int maxComponentSize = i + 1 + initialVectorComponentSize;
			vectors[i * dimensions] = rand.nextInt(maxComponentSize);
			//TODO circular vector selection
			if(rand.nextBoolean()) vectors[i * 3] *= -1;
			vectors[i * dimensions + 1] = rand.nextInt(maxComponentSize);
			vectors[i * dimensions + 2] = rand.nextInt(maxComponentSize);
			if(vectors[i * dimensions] == 0 && vectors[i * dimensions + 1] == 0)
			vectors[i * dimensions + rand.nextInt(3)] = 1; //provide a unit vector if the 0 vector is given.
			if(i > 0){
				scalars[i] = scalars[i - 1] * ratio;
			}
		}
		
		float trigScalar = TWO_PI / width;
		
		//do it in this order for caching reasons.
		int zIndex = 0, yIndex = 0;
		float noiseValue = 0;
		for(int z = 0; z < width; z++){
			zIndex = z * w1 * w1;
			for(int y = 0; y < width; y++){
				yIndex = y * w1;
				for(int x = 0; x < width; x++){
					//we now have an index and an x, y coordinate pair
					noiseValue = rand.nextFloat() * rWeight;
					for(int i = 0; i < trigFunctions; i++){
						int baseVector = i * dimensions;
						noiseValue += (float)Math.sin((
						x * vectors[baseVector    ] +
						y * vectors[baseVector + 1] +
						z * vectors[baseVector + 2]) *
						trigScalar) * scalars[i];
					}
					noise[zIndex + yIndex + x] = noiseValue;
				}
			}
		}
		
		//and edge repeats
		int lastRow = w1 * width;
		int layer = 0;
		for(int z = 0; z < width; z++){
			layer = z * w1 * w1;
			for(int i = 0; i < width; i++){
				noise[width + i * w1 + layer] = noise[i * w1 + layer];
				noise[lastRow + i + layer] = noise[i + layer];
			}
		}
		
		int lastPlane = w1 * w1 * width;
		for(int y = 0; y <= width; y++){
			int curY = y * w1;
			for(int x = 0; x <= width; x++){
				noise[curY + x + lastPlane] = noise[curY + x];
			}
		}
		
		//TODO corners
		
		scale(noise, min, max);
		return noise;
	}
}
