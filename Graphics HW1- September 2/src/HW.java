
public class HW {

	
	public static void main(String[] args) {
		System.out.println(parseInt("5400")+parseInt("54"));
		TenByTen();//prints out the 10x10 Os and Xs
	}
	static int parseInt(String s){
		int value=0;
		for(int i=s.length()-1; i>=0; i--){//runs the length of the string 
			int v = Character.getNumericValue(s.charAt(i));//converts the symbol to a #
			if(v>=10 || v <0) 
				return (int) Double.NaN;//couldn't be a single digit #
			else 
				value+= (int)(v*(Math.pow(10, (s.length()-(i+1)))));//adds the # to value with the correct weight
		}
		return value;
	}
	static void TenByTen() {
		String Os = "OOOOOOOOOO";
		String Xs = "";
		for(int i=9; i>=0; i--){//runs 10 times
			Os = Os.substring(0, i);//makes the # of Os the same as i
			Xs += "X";//adds an X every time
			System.out.println(Os+Xs);//prints out both Os and Xs
		}
	}
}
