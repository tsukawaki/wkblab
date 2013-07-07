//Sift�����ʂ����߂邽�߂̃N���X
import java.awt.Image;
import java.awt.image.PixelGrabber;

public class sift128 {
	public static void main(String[] args) {
	}
	
	public double[][][] step1(int we, int he,int[][] color, Image img){
		int w=we;
		int h=he;
		int sigma=3;
		
		int[][]L = reL(w,h,color,img);
		//�����܂łŁA�e���W���Ƃ�L�����߂��Ă���B
		double[][]m = reM(w,h,L);
		//�����܂łŁA�e���W���Ƃ�M�����߃����Ă���B
		double[][] theta = reT(w,h,L);
		//�����܂łŁA�e���W���Ƃ�Theta�����߂��Ă���B
		
		double[][][] htheta = new double[4][4][36];	
		for(int i=1; i<4; i++){
			int u=w*i/4;
			for(int x=-sigma; x<=sigma;x++){
				for(int j=1; j<4;j++){
					int v=h*j/4;
					for(int y=-sigma; y<=sigma; y++){
						double G = reG(x,y,sigma);
						for(int t=0; t<36; t++){
							double thetadash=(t+1)*10.0;
							if(theta[u+x][v+y]<=thetadash && theta[u+x][v+y]>=thetadash-10){
								htheta[i][j][t]+=G*m[u+x][v+y];
							}
						}					
					}
				}
			}
		}
		//�����܂łŁA�X�ӏ��̌X���Ƌ��������߂��Ă���B
		
		return htheta;
	}
	
	public double[][] last(int imgnumber, int categorynumber,double[][][] sift2,double[][] x){
		for(int i=1; i<4;i++){
			for(int j=1; j<4;j++){
				for(int t=0; t<128; t++){
					//System.out.print(i+","+j+"�̓_��"+t/8+"�̏ꏊ��"+t%8*45+"�x�̗v�f��"+sift2[i][j][t]+"�ł�\n");			
					int k = 3*i + j -4 ;			
					int a = imgnumber -1;
					if(categorynumber == 0){
						a +=0;
					}if(categorynumber == 1){
						a +=51;
					}if(categorynumber == 2){
						a +=51+38;
					}if(categorynumber == 3){
						a +=51+38+53;
					}if(categorynumber == 4){
						a +=51+38+53+35;
					}
					x[a*9+k][t]=sift2[i][j][t];
				}
			}
		}	
		return x;
	}
	
	public double [][][] sift(int w, int h,int [][] step2theta,double [][]step2edge,int[][] color, Image img){
		//�ϐ��̒�`
		int i;int j;int u;int v;int t;
		int x;int y;double theta;
		double G; double[][] m; int[][] L; double rotx; double roty;int xdash;int ydash;
		int segment;
		int sigma=4;
		double[][][] block = new double[4][4][128];
		

		L = reL(w,h, color, img);
		m = reM(w,h,L);
		double[][] thetaa = reT(w,h,L);
		//thetaa�Ɋp�x�������Ă��܂��B	
		
		
		//128:0~7,8~15,,,���ꂼ��ɒl�����悤�B
		//�O�x�@�S�T�x�@�X�O�x�@�P�R�T�x�@�P�W�O�x�@�Q�Q�T�x�@�Q�V�O�x�@�R�P�T�x�̔������ł��B�@
		for(i=1;i<4;i++){
			u=i*w/4;
			//System.out.print("i:"+i);
			for(j=1;j<4;j++){
				v=j*h/4;
				//System.out.print("j:"+j);
				for(x=-sigma; x<+sigma; x++){
					for(y=-sigma ;y<+sigma; y++){
						G = reG(x,y,sigma);
						theta = step2theta[i][j];
						//edge = step2edge[i][j];
						
						//System.out.println("before "+theta);
						theta = theta/360 * 2* Math.PI;
						//360�x��2�΂ɕϊ�
						
						rotx = Math.cos(theta)*(x+0.5) + Math.sin(theta)*(y+0.5);
						roty = Math.sin(theta)*(x+0.5)*-1 + Math.cos(theta)*(y+0.5);
						//System.out.println("theta:"+theta);
						//System.out.println("  sin:"+Math.sin(theta));
						//System.out.println("  cos:"+Math.cos(theta));
						
						xdash = (int)Math.round(rotx); ydash = (int)Math.round(roty);
						//System.out.print("u:"+u+" v:"+v);
						//�l�̌ܓ�
						
						int udash=u+xdash; int vdash=v+ydash;
						//System.out.print("i:"+i+" j:"+j+" u:"+u+" v:"+v+" theta:"+theta+"\n");
						//System.out.println(" udash:"+udash+" vdash:"+vdash+" theta:"+theta*360/(2*Math.PI));
						//System.out.print("rotx:"+rotx+" roty:"+roty);
						//System.out.print("�@x:"+x+" y:"+y+"�@���@");
						//System.out.print("�@xdash:"+xdash+" ydash:"+ydash+"\n");						
						int ii=(x+sigma)/2; int jj=(y+sigma)/2;
						segment= ii + 4*jj;
						for(t=0; t<8; t++){							
							if(thetaa[udash][vdash]>= t*45 && thetaa[udash][vdash]<= (t+1)*45){
								block[i][j][segment*8+t] +=m[udash][vdash]*G;								
							}
						}
					}
				}
			}			
		}
		return block;
	}
	public int[][] getColor(Image img, int color[][], int w, int h){ // �J���[��f���o��
		int[] pixel = new int[w*h];
		PixelGrabber pg = new PixelGrabber(img,0,0,w,h,pixel,0,w);
		try {
			pg.grabPixels();
		} catch (Exception e) {
			
		}
		for (int i=0; i < h; i++) {
			for (int j=0; j < w; j++) {
				color[j+i*w][0] = (pixel[j+i*w] & 0xff0000) >> 16;	// R�l
				color[j+i*w][1] = (pixel[j+i*w] & 0xff00) >> 8;		// G�l
				color[j+i*w][2] = pixel[j+i*w] & 0xff;				// B�l
			}
		}
		return color;
	}
	
	public int[][] reL(int w, int h, int[][] color, Image img){
		//���A�c����͂���Ɓ@�Q�����z���L���Ԃ��Ă��郁�\�b�h
		int sigma=3;
		int sisigma=sigma*sigma;
		int sigma2=sisigma*2;
		
		color = new int[w*h][3];
		color = getColor(img, color, w, h);
		int [][]L = new int[w][h];
		double [][]I = new double[w][h];
		for (int u =0; u<w ; u++){
			for(int v=0; v<h; v++){				
				L[u][v]=0;
				for(int x=-sigma; x<=sigma; x++){
					for(int y=-sigma; y<=sigma; y++){
						if(u+x<0 || v+y<0 || u+x>=w || v+y>=h){
							continue;
						}
						double xy = x*x+y*y;
						double a=Math.exp(-xy/sigma2);
						double b =2.0*Math.PI*sisigma;						
						double G= a/b;
						
						double red=color[u+v*w][0];
						double gle=color[u+v*w][1];
						double blu=color[u+v*w][2];
						I[u+x][v+y]=red*0.257 + 0.504*gle + 0.098*blu + 16;
						//RGB���P�x�ɕϊ�
						L[u][v]+=G*I[u+x][v+y];					
					}
				}
			}
		}
		return L;
	}
	
	public double reG(int x, int y, int sigma){
		double xy = x*x + y*y;
		int sisigma=sigma*sigma;
		int sigma2=sisigma*2;
		double a=Math.exp(-xy/sigma2);
		double b =2*Math.PI*sisigma;						
		double G= a/b;
		return G;
	}
	
	public double[][] reM(int w, int h, int[][] L){
		double[][]m = new double[w][h];
		for(int u=0; u<w; u++){
			for(int v=0; v<h; v++){
				if(u==0 || v==0 || u==w-1 || v==h-1){
				}else{
					double fu = L[u+1][v]-L[u-1][v];
					double fv = L[u][v+1]-L[u][v-1];		
					double fufu = fu*fu;
					double fvfv = fv*fv;
					m[u][v]=Math.sqrt(fufu+fvfv);
				}				
			}
		}
		return m;
	}
	
	public double[][] reT(int w, int h, int[][] L){
		double[][]theta = new double[w][h];
		for(int u=0; u<w; u++){
			for(int v=0; v<h; v++){
				if(u==0 || v==0 || u==w-1 || v==h-1){
				}else{
					double fu = L[u+1][v]-L[u-1][v];
					double fv = L[u][v+1]-L[u][v-1];
					double c = Math.atan(fu/fv)/Math.PI;
					c = c+0.5;
					c = c*360;
					theta[u][v]=c;
				}				
			}
		}
		return theta;
	}

}
