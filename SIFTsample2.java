import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class SIFTsample2 extends JPanel{
	/*
	 * �N���X�^�����O����
	 */
	private static final long serialVersionUID = 1L;
	
	int categoryamount = 2;//�J�e�S���[�̐�(�ő吔��5)
	int clusteramount =3;//�N���X�^�̐�	
	int point = 9;//��_�̐��B
	double[][] x = new double[9*(51+38+53+35+75)][128];
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Title");
		f.getContentPane().add(new SIFTsample2());
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.setSize(400, 400);
		f.setVisible(true);
	}
	public SIFTsample2(){
		
		//x�ɁA�I�������J�e�S���̂��ׂẲ摜��SIFT�����ʂ�����B		
		first first = new first();
		double[][] x = first.firststep(categoryamount, clusteramount, point);		
		//���ׂẲ摜�ɑ΂��鏈�������Bx��SIFT�������Ă��܂��B
		
		//System.out.println("-------�N���X�^�����O�����J�n--------");
		clustering clustering = new clustering();		
		double[][] m = new double[clusteramount][128];		
		int[] c = clustering.readclustering(x,clusteramount,m);					
		//System.out.println("-------�N���X�^�����O�����I���--------");		

		//System.out.println("-------�x�N�g�����̕���--------");			
		double[][] K = clustering.readK(categoryamount,clusteramount,c);
		//�J�e�S���[���̕��σN���X�^�̊����͎Z�o�����BK[category][cluster]�ɓ����Ă��܂��B
		
		//���͉摜�̕��ށB		
		separate_by_cluster sbc = new separate_by_cluster();
		int result[][] =sbc.result(categoryamount, clusteramount, point, c, K);		
		//result�ɃN���X�^�����O�ɂ�镪�ނ��s�����f�[�^�������Ă��܂��B
		//result[categoryamount][clusteramount]�ł��B

		category category = new category();		
		for(int i=0;i<categoryamount;i++){
			String categoryname1 = category.readcategoryname(i);
			for(int j=0;j<categoryamount;j++){
				String categoryname2 = category.readcategoryname(j);
				System.out.println(categoryname1+"�J�e�S���[�̉摜�� "+categoryname2+"�J�e�S���[�ɕ��ނ��ꂽ�摜�̐��� "+result[i][j]+"����܂��B");
			}
			System.out.println("");
			
		}
	}
}


