package Testing;

class node
{
	int data;
	void set(int val) {
		this.data = val;
	}
}

public class Tester_old {
	
	int a, b, c;
	static int square(int val) {
		String variableinmethod = "";
		for(int i=0; i<10; i++)
			System.out.print(i+" ");
		return val*val;
	}
	static int cube(int val) {
		return square(val)*val;
	}
	
	public static void main(String[] args) {
		
		int d = 10;
		
		System.out.print("Square of " + d + ": ");
		
		int ans = square(d);
		System.out.println(ans);
		
		System.out.print("Cube of " + d + ": ");
		int ans2 = cube(d);
		System.out.println(ans2);
		
	}
}
