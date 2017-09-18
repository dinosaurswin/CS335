
public class TenByTen {

	public static void main(String[] args) {
		String Os = "OOOOOOOOO";
		String Xs = "";
		System.out.println(Os+"O");
		for(int i=9; i>0; i--){
			Os = Os.substring(0, i);
			Xs += "X";
			System.out.println(Os+Xs);
			}
		System.out.println(Xs+"X");
	}

}
