import java.util.Random;
//import java.math.*;

public class clustering {	
	public static void main(String[] args) {
	}
	public int[] readclustering(double[][] x ,int clusteramount, double[][] m){
		//ベクトルが入っているx　クラスタ数を決めるK　平均値meanが入ってるm
		System.out.println("-------クラスタリング処理始まり--------\n");
		int D = x[0].length;//→１28が入る
		int B = x.length;//ベクトル。2500くらいの数がはいる。
		System.out.println("D : "+D);
		System.out.println("B : "+B);
		int[] c = new int[B];
		for (int n=0; n<B; n++){
			Random rnd = new Random();
			int ran = rnd.nextInt(clusteramount);
			c[n] = ran;//ran には　0　~　k-1　までの整数値が入ります。	
		}
		
		for(int iteration = 0; iteration<100; iteration++){
			//mの初期化			
			for(int k=0; k<clusteramount; k++){
				for(int d=0; d<D; d++){
					m[k][d] = 0.0 ;
				}
			}
			
			int[]N = new int[clusteramount];
			for(int n=0; n<B; n++){
				for(int d=0; d<D ;d++){
					m[c[n]][d] += x[n][d];
				}
				N[c[n]]++;
			}			
			for(int k=0; k<clusteramount; k++){
				if(N[k] != 0){
					for(int d=0; d<D; d++){
						m[k][d] /= N[k];
					}
				}
			}
			for(int n=0; n<B; n++){	
				double[] dist = new double[clusteramount];
				for (int k=0; k<clusteramount; k++){					
					double dis=0.0;
					double dissum=0.0;
					for(int d=0; d<D;d++){
						dis = (x[n][d]) - (m[k][d]);
						dis = dis*dis;
						dissum += dis;
					}
					//dissum = Math.sqrt(dissum);
					dissum = Math.sqrt(dissum);
					dist[k] = dissum;
				}
				
				int min =0;
				for(int k=0; k<clusteramount; k++){
					if(dist[min] > dist[k]){
						min = k;
					}
				}
				c[n] = min;
			}
		}					
		return c;
	}
	
	public double[][] readK(int categoryamount, int clusteramount, int[] c){
		int startpage = 0;
		int finpage =0;
		category category = new category();
		double K[][] = new double[categoryamount][clusteramount];//カテゴリーの数、クラスターの数だけ
		double N[] = new double[categoryamount];
		//カテゴリー毎の平均クラスタリングの値を求める
		for(int catenum=0; catenum<categoryamount; catenum++){
			startpage = category.startpage(catenum);
			finpage = category.finpage(catenum);
			
			for(int i=startpage; i<finpage; i++){
				K[catenum][c[i]] += 1;
				N[catenum] += 1;
			}
			//K[][]に、クラスタ数が入っている。
			//第一引数はカテゴリ番号。第二引数はクラスタ番号。
			
			System.out.print(catenum+"番目のカテゴリーに含まれる平均的な\n");
			for(int i=0; i<clusteramount; i++){
				K[catenum][i] /= N[catenum];
				System.out.println(i+"　クラスターの割合は"+K[catenum][i]);
			}
		}
		return K;
	}
}
