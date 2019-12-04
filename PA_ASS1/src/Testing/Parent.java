package Testing;

public class Parent {
	static int x;
	Parent ()
	{
		
	}
	Parent (int t)
	{
		x=t;
	}
	static void print(String s)
	{ 
		System.out.println("Print " + s);
	}
	static void display(int a, int b)
	{
		int c = a + b;
		System.out.println("Display" + c);
	}
}