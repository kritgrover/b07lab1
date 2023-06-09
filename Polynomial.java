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
		String line = reader.readLine();
		reader.close();

		String[] terms;
		terms = line.split("(?=[+-])");

		double[] parsedCoefficients = new double[terms.length];
		int[] parsedExponents = new int[terms.length];

		for (int i = 0; i < terms.length; i++) {
			String term = terms[i];
			int caretIndex = term.indexOf('x');

			if (caretIndex != -1) {
				parsedCoefficients[i] = Double.parseDouble(term.substring(0, caretIndex));
				parsedExponents[i] = Integer.parseInt(term.substring(caretIndex + 1));
			} else {
				parsedCoefficients[i] = Double.parseDouble(term);
				parsedExponents[i] = 0;
			}
		}

		this.coeff = parsedCoefficients;
		this.powers = parsedExponents;
	}

	public Polynomial add(Polynomial p){
		int length = this.coeff.length + p.coeff.length;
		double[] newCoeff = new double[length];
		int[] newPowers = new int[length];

		for (int i = 0; i < this.coeff.length; i++) {
			newCoeff[this.powers[i]] += this.coeff[i];
			newPowers[this.powers[i]] = this.powers[i];
		}

		for (int i = 0; i < p.coeff.length; i++) {
			newCoeff[p.powers[i]] += p.coeff[i];
			newPowers[p.powers[i]] = p.powers[i];
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

	public void saveToFile(String fileName) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < coeff.length; i++) {
				if (coeff[i] != 0) {
					if (coeff[i] > 0 && i != 0) {
						sb.append("+");
					}

					sb.append(coeff[i]);

					if (powers[i] != 0) {
						sb.append("x");

						if (powers[i] != 1) {
							sb.append(powers[i]);
						}
					}
				}
			}

			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}