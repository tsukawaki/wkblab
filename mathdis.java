
public class mathdis {
	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}
	public double[] distance(double K[][],double Ks[],int catenum, int clusteramount, int point,int categoryamount){
		double[] dist = new double[categoryamount];
		double dis=0.0;
		double dissum=0.0;
		
		for(int i=0; i<categoryamount;i++){
			for(int k=0; k<clusteramount;k++){
				dis = (Ks[k]/point)-(K[i][k]);
				System.out.println("Ks�̒l:"+Ks[k]/point+" K�̒l:"+K[i][k]);
				dis = dis*dis;
				dissum += dis;
				//dissum = Math.sqrt(dissum);
			}
			dist[i] = dissum;
			dissum =0;
			dis = 0;
		}
		return dist;
	}

}
