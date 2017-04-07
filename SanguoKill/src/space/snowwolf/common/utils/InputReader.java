package space.snowwolf.common.utils;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputReader {

	private Scanner sc;
	
	public Scanner getScanner() {
		return sc;
	}
	
	public void init(InputStream in) {
		sc = new Scanner(in);
	}
	
	public int choose(String title, String ... options) {
		while(true) {
			if(title != null) {
				System.out.println(title);
			}
			for(int i=0;i<options.length;i++) {
				System.out.println("[" + (i+1) + "]" + options[i]);
			}
			int t;
			try {
				t = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("输入有误，请重试");
				sc.nextLine();
				continue;
			}
			if(t > 0 && t <= options.length) {
				return t;
			}
		}
	}
	
}
