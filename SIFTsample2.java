import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class SIFTsample2 extends JPanel{
	/*
	 * クラスタリングする
	 */
	private static final long serialVersionUID = 1L;
	
	int categoryamount = 2;//カテゴリーの数(最大数は5)
	int clusteramount =3;//クラスタの数	
	int point = 9;//交点の数。
	double[][] x = new double[9*(51+38+53+35+75)][128];
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Title");
		f.getContentPane().add(new SIFTsample2());
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.setSize(400, 400);
		f.setVisible(true);
	}
	public SIFTsample2(){
		
		//xに、選択したカテゴリのすべての画像のSIFT特徴量が入る。		
		first first = new first();
		double[][] x = first.firststep(categoryamount, clusteramount, point);		
		//すべての画像に対する処理おわり。xにSIFTが入っています。
		
		//System.out.println("-------クラスタリング処理開始--------");
		clustering clustering = new clustering();		
		double[][] m = new double[clusteramount][128];		
		int[] c = clustering.readclustering(x,clusteramount,m);					
		//System.out.println("-------クラスタリング処理終わり--------");		

		//System.out.println("-------ベクトル毎の分類--------");			
		double[][] K = clustering.readK(categoryamount,clusteramount,c);
		//カテゴリー毎の平均クラスタの割合は算出完了。K[category][cluster]に入っています。
		
		//次は画像の分類。		
		separate_by_cluster sbc = new separate_by_cluster();
		int result[][] =sbc.result(categoryamount, clusteramount, point, c, K);		
		//resultにクラスタリングによる分類を行ったデータが入っています。
		//result[categoryamount][clusteramount]です。

		category category = new category();		
		for(int i=0;i<categoryamount;i++){
			String categoryname1 = category.readcategoryname(i);
			for(int j=0;j<categoryamount;j++){
				String categoryname2 = category.readcategoryname(j);
				System.out.println(categoryname1+"カテゴリーの画像中 "+categoryname2+"カテゴリーに分類された画像の数は "+result[i][j]+"個あります。");
			}
			System.out.println("");
			
		}
	}
}


