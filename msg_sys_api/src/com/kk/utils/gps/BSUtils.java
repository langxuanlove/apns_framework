package com.kk.utils.gps;

import java.util.ArrayList;
import java.util.List;



public class BSUtils {
	public List<String> main(int argc, String argv[]) {
		List<String>  list=new ArrayList<String>();
		double[] lat = new double[7];
		double[] lon = new double[7];
		double[] distance_weight = new double[7];
		double distance_prod = (float) 1.0;
		double distance_sum = (float) 0.0;
		double rxlevel = 0;
		double freq =1000;
		int count = 0;
		int i=0;
		double x, y, z;
		double x_avg, y_avg, z_avg;
		double lat_avg, lon_avg;
		for (i = 0; i < argc; i += 4) {
			lat[count] = Double.parseDouble(argv[i]) / 180.0 * Math.PI;
			lon[count] = Double.parseDouble(argv[i + 1]) / 180.0 * Math.PI;
			try {
				rxlevel =-Double.parseDouble(argv[i + 2]);
			} catch (Exception e) {
				rxlevel =-Double.parseDouble(Integer.valueOf(argv[i + 2],16).toString());
			}
			System.out.println("rxlevel"+rxlevel);
			freq = 1000;
			distance_weight[count] = Math.pow(10.0,
					(130.0 + rxlevel - 20.0 * Math.log10(freq)) / 20.0);
			distance_prod *= distance_weight[count];
			distance_sum += distance_weight[count];
			count++;
		}
		for (i = 0; i < count; i++) {
			distance_weight[i] = distance_prod / distance_weight[i];
			// printf("%f\n",distance_weight[i]);
		}
		if (count == 0) {
			return null;
		}
		x = 0.0;
		y = 0.0;
		z = 0.0;
		for (i = 0; i < count; i++) {
		    x +=Math.cos(lat[i]) *Math.cos(lon[i]) * distance_weight[i];
		    y +=Math.cos(lat[i]) *Math.sin(lon[i]) * distance_weight[i];
		    z +=Math. sin(lat[i]) * distance_weight[i];
		  }
		x_avg = x/distance_sum;
		y_avg = y/distance_sum;
		z_avg = z/distance_sum;
		lat_avg =Math.atan(z_avg / Math.sqrt(x_avg * x_avg + y_avg * y_avg)) * 180.0 / Math.PI;
		lon_avg = Math.atan(y_avg / x_avg) * 180.0 / Math.PI;
		if (lon_avg < 0) lon_avg += 180.0;
		System.out.println("lat_avg"+lat_avg);
		System.out.println("lon_avg"+lon_avg);
		list.add(lat_avg+"");
		list.add(lon_avg+"");
		return list;
	}

}
