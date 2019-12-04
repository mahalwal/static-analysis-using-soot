package Testing;
import Testing.Parent;

public class Tester extends Parent {
	int c = 0;
	Tester()
	{
		super();
	}
	static int loop = 0;
	static int status = 1;
	static Parent p = new Parent ();
	
	static void print ()
	{
		while(loop<5) {
			System.out.println("Print "+loop);
			loop++;
		}
		
		for (int j=0; j<5; j++)
			System.out.println("Print "+j);
		
		int k=0;
		do {
			System.out.println("Print "+k);
			k++;
		}while(k<5);
	}
	
	static int show(int a, float f)
	{
		int c;
		if (a > 1)
			c = 2 * a;
		else
			c = a * -1;
		System.out.println("f:" + f);
		return c;
	}
	public static void main(String [] args) {
		p.display(1, 11);
		p.print("child");
		int b = show (1, 1);
		print();
		System.out.println("Returned" + b);
	}
}