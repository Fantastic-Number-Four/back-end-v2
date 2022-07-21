import java.util.ArrayList;
import java.util.List;

public class app {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String S = "111100";
		int result = 100;
		while((int)(Math.log10(result)+1) > 1) {
			List<Integer> res = new ArrayList<Integer>();
			int s = Integer.parseInt(S);
			
			for(int x = 0 ; x < S.length(); x++) {
				res.add( s % 10 );
				s /= 10;
			}
			result = 0;
			
			int length = S.length();
			
			int count = 1;
			
			for(int x = res.size()-1; x >= 0; x--) {
				
				if (count < length+1) {
					result +=  (res.get(x)*count);
					count ++;
				}
				
			}
			
			S = "" + result;
//			System.out.println(S);
			
			
		}
	
		System.out.println(result);
		
		
		
		
//		Arrays.stream(array).distinct().toArray(String[]::new);
		
	}

}
