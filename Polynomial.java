import java.io.*;
import java.util.Arrays;

public class Polynomial{
	
	public double[] coeff;
	public int[] powers;

	public Polynomial(){
		this.coeff = new double[] {0};
		this.powers = new int[] {0};
	}

	public Polynomial(double[] coeff, int[] powers){
		this.coeff = coeff;
		this.powers = powers;
	}

	public Polynomial(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String poly = reader.readLine();
		reader.close();

		String[] terms;
		terms = poly.split("(?=[+-])");

		double[] newCoeffs = new double[terms.length];
		int[] newPowers = new int[terms.length];

		for (int i = 0; i < terms.length; i++) {
			String term = terms[i];
			int splitIndex = term.indexOf('x');

			if (splitIndex != -1) {
				newCoeffs[i] = Double.parseDouble(term.substring(0, splitIndex));
				newPowers[i] = Integer.parseInt(term.substring(splitIndex + 1));
			} else {
				newCoeffs[i] = Double.parseDouble(term);
				newPowers[i] = 0;
			}
		}

		this.coeff = newCoeffs;
		this.powers = newPowers;
	}

	public Polynomial add(Polynomial p){
		int length = this.coeff.length + p.coeff.length;
		double[] newCoeff = new double[length];
		int[] newPowers = new int[length];

		int i = 0, j = 0, k = 0;

		while (i < coeff.length && j < p.coeff.length) {
			if (this.powers[i] < p.powers[j]) {
				newCoeff[k] = this.coeff[i];
				newPowers[k] = this.powers[i];
				i++;
			} else if (this.powers[i] > p.powers[j]) {
				newCoeff[k] = p.coeff[j];
				newPowers[k] = p.powers[j];
				j++;
			} else {
				newCoeff[k] = this.coeff[i] + p.coeff[j];
				newPowers[k] = this.powers[i];
				i++;
				j++;
			}
			k++;
		}

		while (i < coeff.length) {
			newCoeff[k] = this.coeff[i];
			newPowers[k] = this.powers[i];
			i++;
			k++;
		}

		while (j < p.coeff.length) {
			newCoeff[k] = p.coeff[j];
			newPowers[k] = p.powers[j];
			j++;
			k++;
		}

		return returnTrimmed(newCoeff, newPowers, length);
	}
	
	public double evaluate(double x) {
		double res = 0;
		for(int i = 0; i < this.coeff.length; i++) {
			res += this.coeff[i] * (Math.pow(x, this.powers[i]));
		}
		return res;
	}
	
	public boolean hasRoot(double x) {
		return (this.evaluate(x) == 0);
	}

	public Polynomial multiply(Polynomial p) {
		int a = this.powers.length;
		int b = p.powers.length;
		int newLength = a + b + 1;

		double[] newCoeff = new double[newLength];
		int[] newPowers = new int[newLength];

		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				int newExponent = this.powers[i] + p.powers[j];
				newCoeff[newExponent] += (this.coeff[i] * p.coeff[j]);
				newPowers[newExponent] = newExponent;
			}
		}

		return returnTrimmed(newCoeff, newPowers, newLength);
	}

	public Polynomial returnTrimmed(double[] coeffs, int[] powers, int length){
		int finalLength = 0;
		double[] finalCoeff = new double[length];
		int[] finalPowers = new int[length];
		for (int i = 0; i < length; i++) {
			if (coeffs[i] != 0) {
				finalCoeff[finalLength] = coeffs[i];
				finalPowers[finalLength] = powers[i];
				finalLength++;
			}
		}

		finalCoeff = Arrays.copyOf(finalCoeff, finalLength);
		finalPowers = Arrays.copyOf(finalPowers, finalLength);

		return new Polynomial(finalCoeff, finalPowers);
	}

	public void saveToFile(String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

		boolean isFirstTerm = true;

		for (int i = 0; i < this.coeff.length; i++) {
			if (this.coeff[i] != 0) {
				if (!isFirstTerm) {
					writer.write(this.coeff[i] > 0 ? "+" : "-");
				} else {
					isFirstTerm = false;
				}

				writer.write(Double.toString(Math.abs(this.coeff[i])));

				if (this.powers[i] > 0) {
					writer.write("x" + this.powers[i]);
				}
			}
		}

		writer.close();
	}
}