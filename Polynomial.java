public class Polynomial{
	
	public double[] coeff;
	
	public Polynomial(){
		this.coeff = new double[] {0};
		// coeff[0] = 0;
	}

	public Polynomial(double[] arr){
		this.coeff = arr;
	}
	
	public Polynomial add(Polynomial p){
		Polynomial s = new Polynomial();
		
		if(p.coeff.length > this.coeff.length) {
			s.coeff = p.coeff;
			for(int i = 0; i < this.coeff.length; i++) {
				s.coeff[i] += this.coeff[i];
			}
			
		} else {
			s.coeff = this.coeff;
			for(int i = 0; i < p.coeff.length; i++) {
				s.coeff[i] += p.coeff[i];
			}
		}
		
		
		return s;
	}
	
	public double evaluate(double x) {
		double res = 0;
		for(int i = 0; i < this.coeff.length; i++) {
			res += this.coeff[i] * (Math.pow(x, i));
		}
		return res;
	}
	
	public boolean hasRoot(double x) {
		return (this.evaluate(x) == 0);
	}

}