
	/*  gram 51
    panda 38
    pizza 53
    snoopy 35
    umbrella 75
   	*/
	
public class category {
	public static void main(String[] args) {
	}
	
	public String readcategoryname(int x){
		String k = "";
		if(x==0){
			k = "gramophone";
		}if(x==1){
			k = "panda";
		}if(x==2){
			k = "pizza";
		}if(x==3){
			k = "snoopy";
		}if(x==4){
			k = "umbrella";
		}		
		return k;
	}
	
	public int startpage(int x){
		int startpage = 0;
		if(x==0){
			//c[n]‚Ì”ÍˆÍ‚Í0~458				
		}if(x==1){
			startpage =459;		
			//c[n]‚Ì”ÍˆÍ‚Í459~800				
		}if(x==2){
			startpage =801;		
			//c[n]‚Ì”ÍˆÍ‚Í801~1278				
		}if(x==3){
			startpage =1279;		
			//c[n]‚Ì”ÍˆÍ‚Í1279~1592				
		}if(x==4){
			startpage =1593;		
			//c[n]‚Ì”ÍˆÍ‚Í1593~2268				
		}
		return startpage;
	}
	
	public int finpage(int x){
		int finpage=0;
		if(x==0){
			finpage =459;
			//c[n]‚Ì”ÍˆÍ‚Í0~458				
		}if(x==1){
			finpage =801;				
			//c[n]‚Ì”ÍˆÍ‚Í459~800				
		}if(x==2){
			finpage =1279;				
			//c[n]‚Ì”ÍˆÍ‚Í801~1278				
		}if(x==3){
			finpage =1593;				
			//c[n]‚Ì”ÍˆÍ‚Í1279~1592				
		}if(x==4){
			finpage =2268;				
			//c[n]‚Ì”ÍˆÍ‚Í1593~2268				
		}
		return finpage;
	}
	
	public int page(int x){
		int page = 0;
		if(x == 0){
			page=51;
		}if(x == 1){
			page=38;
		}if(x == 2){
			page=53;
		}if(x == 3){
			page=35;
		}if(x == 4){
			page=75;
		}
		return page;
	}
}