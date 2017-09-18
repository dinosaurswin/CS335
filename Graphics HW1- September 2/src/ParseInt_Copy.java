
public class ParseInt_Copy {

	public static void main(String[] args) {
		System.out.println(parseInt("1"));
		System.out.println(parseInt("21"));
		System.out.println(parseInt("321"));
		System.out.println(parseInt("4321"));
		System.out.println(parseInt("lol"));
	}
	static int parseInt(String s){
		int value=0;
		for(int i=s.length()-1; i>=0; i--){
			int v = Character.getNumericValue(s.charAt(i));
			if(v>=10 || v <0) return (int) Double.NaN;//couldn't be a single digit #
			else value+= (int)(v*(Math.pow(10, (s.length()-(i+1)))));
		}
		return value;
	}

}
