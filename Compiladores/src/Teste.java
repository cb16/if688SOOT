
public class Teste {

	public static void a(){
		
	}
	
	public static void c() {
		
	}
	
	public static void main(String[] args){
		a();
		Other tst = new Other();
		tst.b();
		Classe c = new Classe();
		c.met();
	}
	
}

class Classe {
	public void met() {
		
	}
}

class Other extends Teste{
	public void b() {
		
	}
}
