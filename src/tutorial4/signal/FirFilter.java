package tutorial4.signal;

public class FirFilter {

	double coeffs5[] = { 
			0.117119343653851,
			0.129091809713508,
			0.133251953115574,
			0.129091809713508,
			0.117119343653851
	};

	double coeffs[] = { 
			-0.006907390651426909,
			-0.005312580651013577,
			-0.002644739091749330,
			 0.001183336967173867,
			 0.006192514984703184,
			 0.012327624024018900,
			 0.019453074574047193,
			 0.027354594681004485,
			 0.035747363433714395,
			 0.044290256550950494,
			 0.052605352818230700,
			 0.060301340430963822,
			 0.066999061029964030,
			 0.072357178973056519,
			 0.076095892581739766,
			 0.078016723675218447,
			 0.078016723675218447,
			 0.076095892581739766,
			 0.072357178973056519,
			 0.066999061029964030,
			 0.060301340430963822,
			 0.052605352818230700,
			 0.044290256550950494,
			 0.035747363433714395,
			 0.027354594681004485,
			 0.019453074574047193,
			 0.012327624024018900,
			 0.006192514984703184,
			 0.001183336967173867,
			-0.002644739091749330,
			-0.005312580651013577,
			-0.006907390651426909
	};

	double coeffs128[] = { 
	 92.69223127575062900E-6,
	 117.4139230236271060E-6,
	 136.1603445582950940E-6,
	 145.7913866871544710E-6,
	 144.2150428901059910E-6,
	 130.8118486263338980E-6,
	 106.6807406421779090E-6,
	 74.65083315336072190E-6,
	 39.03228154373359620E-6,
	 5.114893770217857850E-6,
	-21.53893564293055010E-6,
	-35.92520945020286400E-6,
	-34.44429044717595900E-6,
	-15.63840802447906240E-6,
	 19.30115939239187380E-6,
	 66.35996718684546640E-6,
	 118.9261805969918130E-6,
	 168.4335290097706380E-6,
	 205.3808982960431420E-6,
	 220.6289483924978190E-6,
	 206.8217744805918410E-6,
	 159.7438689751278960E-6,
	 79.40629953788123880E-6,
	-29.33418543453757370E-6,
	-156.7816729133389800E-6,
	-289.1277220503002920E-6,
	-409.9074652686587680E-6,
	-502.0967282111139410E-6,
	-550.6622981696729080E-6,
	-545.2888095037574200E-6,
	-482.9402231537033000E-6,
	-369.8824474320829840E-6,
	-222.8041876928859040E-6,
	-68.72931374782892530E-6,
	 56.48546740122091590E-6,
	 111.1357725681478430E-6,
	 52.04180946925241360E-6,
	-159.9617139971219900E-6,
	-553.8525103949224330E-6,
	-0.001142135834352444,
	-0.001915109922808091,
	-0.002836373888318093,
	-0.003840230264342132,
	-0.004831522071148391,
	-0.005688264653286233,
	-0.006267198282435876,
	-0.006412117978797253,
	-0.005964556633782519,
	-0.004776133615336287,
	-0.002721661065145478,
	 288.0511676515272370E-6,
	 0.004294814179322214,
	 0.009284187815428738,
	 0.015180064213083658,
	 0.021843419804521955,
	 0.029075636798045750,
	 0.036626430951879105,
	 0.044206037210696506,
	 0.051500933133884388,
	 0.058192056552172355,
	 0.063974229592461823,
	 0.068575360540093194,
	 0.071773973250860712,
	 0.073413715463905710,
	 0.073413715463905710,
	 0.071773973250860712,
	 0.068575360540093194,
	 0.063974229592461823,
	 0.058192056552172355,
	 0.051500933133884388,
	 0.044206037210696506,
	 0.036626430951879105,
	 0.029075636798045750,
	 0.021843419804521955,
	 0.015180064213083658,
	 0.009284187815428738,
	 0.004294814179322214,
	 288.0511676515272370E-6,
	-0.002721661065145478,
	-0.004776133615336287,
	-0.005964556633782519,
	-0.006412117978797253,
	-0.006267198282435876,
	-0.005688264653286233,
	-0.004831522071148391,
	-0.003840230264342132,
	-0.002836373888318093,
	-0.001915109922808091,
	-0.001142135834352444,
	-553.8525103949224330E-6,
	-159.9617139971219900E-6,
	 52.04180946925241360E-6,
	 111.1357725681478430E-6,
	 56.48546740122091590E-6,
	-68.72931374782892530E-6,
	-222.8041876928859040E-6,
	-369.8824474320829840E-6,
	-482.9402231537033000E-6,
	-545.2888095037574200E-6,
	-550.6622981696729080E-6,
	-502.0967282111139410E-6,
	-409.9074652686587680E-6,
	-289.1277220503002920E-6,
	-156.7816729133389800E-6,
	-29.33418543453757370E-6,
	 79.40629953788123880E-6,
	 159.7438689751278960E-6,
	 206.8217744805918410E-6,
	 220.6289483924978190E-6,
	 205.3808982960431420E-6,
	 168.4335290097706380E-6,
	 118.9261805969918130E-6,
	 66.35996718684546640E-6,
	 19.30115939239187380E-6,
	-15.63840802447906240E-6,
	-34.44429044717595900E-6,
	-35.92520945020286400E-6,
	-21.53893564293055010E-6,
	 5.114893770217857850E-6,
	 39.03228154373359620E-6,
	 74.65083315336072190E-6,
	 106.6807406421779090E-6,
	 130.8118486263338980E-6,
	 144.2150428901059910E-6,
	 145.7913866871544710E-6,
	 136.1603445582950940E-6,
	 117.4139230236271060E-6,
	 92.69223127575062900E-6
	};
	
	double[] xv;  // This array holds the delayed values
	double gain = 1;
	int M; // The number of taps, the length of the filter
	
	public FirFilter() {
		M = coeffs.length-1;
		xv = new double[M+1];
	}
	
	public double filter(double in) {
		double sum; 
		int i;
		for (i = 0; i < M; i++) 
			xv[i] = xv[i+1];
		xv[M] = in * gain;
		sum = 0.0;
		for (i = 0; i <= M; i++) 
			sum += (coeffs[i] * xv[i]);
		return sum;
	}
}
