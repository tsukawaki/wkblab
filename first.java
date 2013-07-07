
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class first extends JPanel{
	private static final long serialVersionUID = 1L;
	Image img0, img;
	double[][] x = new double[9*(51+38+53+35+75)][128];
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Title");
		f.getContentPane().add(new SIFTsample2());
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.setSize(400, 400);
		f.setVisible(true);
	}
	public double[][] firststep(int categoryamount, int clusteramount, int point){
		
		//xに、選択したカテゴリのすべての画像のSIFT特徴量が入る。
		
		for(int categorynumber=0; categorynumber<categoryamount; categorynumber++){
			category category = new category();
			String name = category.readcategoryname(categorynumber);
			int page = category.page(categorynumber);
			
			System.out.println(name+"開始");
			String pass = "C:\\Users\\Yuta\\workspace\\2013keizemi\\src\\"+name+"\\image_"; 
		
			for(int imgnum=1; imgnum<page+1;imgnum++){

				String imgFile= String.format("%04d", imgnum);
				try {
					File file = new File(pass + imgFile+".jpg");
					img  = ImageIO.read(file);
				} catch (Exception e) {
					System.out.println("画像ファイル表示エラー");
					e.printStackTrace();
				}
				int w = img.getWidth(this);
				int h = img.getHeight(this);
				int[][] color = new int[w*h][3];
				getColor(img, color, w, h);
				
				sift128 sift128 = new sift128();
				double[][][] sift = sift128.step1(w,h,color,img);
				//ヒストグラムがsift変数に入っています。ピークは求められていません		
				
				int [][] step2theta = new int[4][4];
				double [][] step2edge = new double[4][4];
				//step2theta変数は、1,1~4,4までに値を持ちます。Thetaを返します。
			
				for(int i=1;i<4;i++){
					for(int j=1; j<4;j++){
						double max=0;int maxt=0;
						for(int t=0; t<36 ;t++){
							if(max < sift[i][j][t]){
								max=sift[i][j][t];
								maxt=t;
							}if(t==35){
								//System.out.println(u+","+v+"の場所におけるピークは"+maxt*10+"度であり、強さは"+max+"である。");
								step2theta[i][j]=maxt*10;
								step2edge[i][j]=max;
							}
						}						
					}						
				}
				double[][][] sift2 = sift128.sift(w,h,step2theta,step2edge,color,img);
				x = sift128.last(imgnum, categorynumber, sift2, x);
				//もしかしたらxの前半が上書きされるかも。要注意！				
			}	
		}
		return x;
	}
	
	public int[][] getColor(Image img, int color[][], int w, int h){ // カラー画素取り出し
		int[] pixel = new int[w*h];
		PixelGrabber pg = new PixelGrabber(img,0,0,w,h,pixel,0,w);
		try {
			pg.grabPixels();
		} catch (Exception e) {
			
		}
		for (int i=0; i < h; i++) {
			for (int j=0; j < w; j++) {
				color[j+i*w][0] = (pixel[j+i*w] & 0xff0000) >> 16;	// R値
				color[j+i*w][1] = (pixel[j+i*w] & 0xff00) >> 8;		// G値
				color[j+i*w][2] = pixel[j+i*w] & 0xff;				// B値
			}
		}
		return color;
	}	
}