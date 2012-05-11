package functions;

import java.util.ArrayList;

import symboldiff.Expression;
import symboldiff.exceptions.ExpressionException;
import net.sourceforge.interval.ia_math.RealInterval;
import core.Box;

public class FunctionNEW {
	protected int dim;
	protected Expression function;
	protected Expression[] d1f;
	protected Expression[] d2f;
	
	protected FunctionNEW() {	}
	
	// init for FunctionFactory
	void init(Expression function, Expression[] d1f, Expression[] d2f) {
		this.function = function;
		this.d1f = d1f;
		this.d2f = d2f;
		this.dim =function.getVariables().size();
		assert( (d1f == null && d2f == null)|| 
				(this.dim == d1f.length && this.dim == d2f.length) );
	}
	// init for legacy functions from functions package
	protected void init(int dim, String equation) {
		FunctionNEW f = null;
		try {
			f = FunctionFactory.newFunction(equation);
		} catch (ExpressionException e) {
			System.out.println("Can't create the following function: " + equation + ". " + e.getMessage());
			System.exit(-1);
		}
		assert(dim == f.getDimension());
		this.setTo(f);	
	}
	protected void setTo(FunctionNEW f) {
		this.function = f.function;
		this.d1f = f.d1f;
		this.d2f = f.d2f;
		this.dim = f.dim;			
	}
	
	public void calculate(Box area) {
		RealInterval f = function.evaluate(area);
		assert(f != null && f.lo() > Double.NEGATIVE_INFINITY && f.hi() < Double.POSITIVE_INFINITY);
		area.setFunctionValue(f);
	}
	public double calculatePoint(double... point) {
		return function.evaluate(point);
	}
	public RealInterval calculate1Derivative(Box box, int argNum) {
		if (d1f == null)
			return null;
		//return d1f[argNum].evaluate(box); // nvp 5/11/12
		RealInterval ii = null; //new RealInterval();
		try {
			ii = d1f[argNum].evaluate(box);
		} catch (Exception e) {
			// something has happened.
			//F.e. division by zero
		}
		return ii;
	}
	public RealInterval calculate2Derivative(Box box, int argNum) {
		if (d2f == null)
			return null;
		// nvp 5/11/12
		//return d2f[argNum].evaluate(box);
		RealInterval ii = null; //new RealInterval();
		try {
			ii = d2f[argNum].evaluate(box);
		} catch (Exception e) {
			// something has happened.
			//F.e. division by zero
		}
		return ii;		
	}	
	public String toString() {
		return function.toString();
	}	
	public int getDimension() {
		return dim;
	}
	public ArrayList<String> getVariables() {
		return function.getVariables();
	}

	public int getVariableNum(String arg) {
		return function.getVariableNum(arg);
	}
}
