package notsureyet;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
public class SLR {

	public int n = 0;
	public List<Pair> dataset = new ArrayList<Pair>();
	
	private double meanx;
	private double meanx2;
	private double meany;
	private double meanxy;
	
	private double variance;
	private double covariance;
	private double slope;
	
	class Pair {
		public double x;
		public double y;
		public Pair (double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	class Model {
		private double slope;
		private double yint;
	
		public Model (){
		}
		public Model (double slope, double yint) {
			this.slope = slope;
			this.yint = yint;
		}
		public void set (double slope, double yint) {
			this.slope = slope;
			this.yint = yint;
		}
		public double eval (double x) {
			return this.yint + this.slope * x;
		}
	}
	
	private void covariance () {
		this.covariance = (this.meanxy - (this.meanx*this.meany));
	}
	private void variance () {
		this.variance = (this.meanx2 - Math.pow(this.meanx,2));
	}
	private void slope() {
		this.slope = this.covariance / this.variance;
	}
	
	private void mean() {
		double meanx = 0;
		double meany = 0;
		double meanxy = 0;
		double meanx2 = 0;
		for (Pair i : this.dataset) {
			meanx += i.x;
			meanx2 += i.x * i.x;
			meany += i.y;
			meanxy += i.x * i.y;
		}
		this.meanx = meanx/this.n;
		this.meanx2 = meanx2/this.n;
		this.meany = meany/this.n;
		this.meanxy = meanxy/this.n;
	}
	public void train(Model m) {
		mean();
		variance();
		covariance();
		slope();
		m.set(this.slope, this.meany - (this.slope * this.meanx)); //approximated slope and y-intercept
	}
	public static void main(String[] args) { 
		SLR slr = new SLR();
		Model m = slr.new Model();
		try {
			Scanner sc = new Scanner(new File("RealEstate.csv"));
			String[] line;
			double x, y;
			while(sc.hasNextLine()) {				// read in csv, tokenize, build x,y dataset
				line = sc.nextLine().split(",");
				x = Double.parseDouble(line[5]);	// dollar price of house
				y = Double.parseDouble(line[2]);	// sqft of house
				slr.dataset.add(slr.new Pair(x, y));
				slr.n++;
			}
			slr.train(m);		// train by performing stats calculations for linear regression formula
			System.out.println("meanx, meany, variance, covariance, model.c0, model.c1\n" + slr.meanx + " " + slr.meany + " " + slr.variance + " " + slr.covariance + " " + m.yint + " " + m.slope);
			System.out.println("Evaluating sqft of 2371: " + m.eval(2371));
			sc.close();
		} catch (FileNotFoundException err) {
			System.err.println(err);
		}
	}

}
