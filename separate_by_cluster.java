//import java.math.*;
public class separate_by_cluster {
	public static void main(){
	}
	public int[][] result(int categoryamount, int clusteramount, int point, int[] c,double[][] K) {
		int[][] solution = new int [categoryamount][clusteramount];
		for(int catenum = 0 ; catenum < categoryamount; catenum++){
			category category = new category();
			
			int start = category.startpage(catenum);
			int fin = category.finpage(catenum);
		
			double Ks[] = new double[clusteramount];
			for(int i =start; i<fin; i=i+9){
				for(int j =0; j<point; j++){
					Ks[c[i+j]] += 1.0;
				}
				//Ksに１画像分のクラスタリング情報が入っている。
			
				mathdis mathdis = new mathdis();
				double[] dist = mathdis.distance(K, Ks,catenum,clusteramount,point,categoryamount);
			
				int min =0;
				for(int j=0; j<categoryamount; j++){
					if(dist[min] > dist[j]){
						min = j;
					}
				}
				String categoryname = category.readcategoryname(catenum);
				System.out.println(categoryname+"のカテゴリーに属し、通し番号"+i/9+"番目の画像はおそらく、"+min+"番目のカテゴリーに属します。");
				solution[catenum][min] += 1;
				for(int j=0;j<clusteramount;j++){
					Ks[j]=0;
				}
			}
			/*
			for(int i=0; i<clusteramount; i++){
				System.out.println(categoryname+" : "+i+" : "+Re[catenum][i]+"");
			}
			*/
		}	
		return  solution;
	}
}
