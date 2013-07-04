using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace LDA
{
	class LDA
	{
		TokenData data;
		int tokenNum;
		int topicNum;
		double alpha;
		double beta;
		/// <summary>
		/// トピックごとの単語出力回数
		/// </summary>
		int[,] countW_Z;
		/// <summary>
		/// 文章ごとのトピック出力回数
		/// </summary>
		int[,] countZ_D;
		/// <summary>
		/// 全文章においてトピックが出る回数
		/// </summary>
		int[] countZ;
		/// <summary>
		/// 現時点での単語ごとのトピックの割り当て
		/// </summary>
		int[][] assign;
		Random random;

		static public LDA getInstance(String filename, int topicNum, double alpha, double beta, int seed)
		{
			return new LDA(filename, topicNum, alpha, beta, seed);
		}

		public LDA(String filename, int topicNum, double alpha, double beta, int seed)
		{
			data = TokenData.loadData(filename);
			this.topicNum = topicNum;
			this.alpha = alpha;
			this.beta = beta;
			random = new Random(seed);
			countW_Z = new int[topicNum, data.wordNum];
			countZ_D = new int[data.docNum(), topicNum];
			countZ = new int[topicNum];
			assign = new int[data.docNum()][];
			tokenNum = 0; // 総単語数

			// 初期のトピックの割り当てをランダムで行う
			for (int n = 0; n < data.docNum(); n++)
			{
				assign[n] = new int[data.tokens(n).Length];
				for (int i = 0; i < data.tokens(n).Length; i++)
				{
					assign[n][i] = random.Next(topicNum);
					addSample(n, i);
					tokenNum++;
				}
			}
		}

		/// <summary>
		/// サンプリングを行う
		/// </summary>
		/// <param name="iteration"></param>
		public void sampling(int iteration)
		{
			for (int it = 0; it < iteration; it++)
			{
				Console.WriteLine("\tLDA iteration " + it);
				long startTime = CurrentTimeMillis();
				for (int n = 0; n < data.docNum(); n++)
				{
					for (int i = 0; i < data.tokens(n).Length; i++)
					{
						removeSample(n, i);
						//sampling
						assign[n][i] = selectTopic(n, i);
						addSample(n, i);
					}
				}
				long endTime = CurrentTimeMillis();
				double time = (endTime - startTime) / 1000.0;
				double wps = (double)tokenNum / time;
				Console.WriteLine("\t\ttime(sec):" + time + "\ttime(word/sec):" + wps);
			}
		}

		/// <summary>
		/// 引数の単語のトピックを選択する
		/// </summary>
		/// <param name="n"></param>
		/// <param name="i"></param>
		/// <returns></returns>
		private int selectTopic(int n, int i)
		{
			double[] prob = new double[topicNum];
			for (int z = 0; z < topicNum; z++)
			{
				prob[z] = ((double)countZ_D[n, z] + alpha) * ((double)countW_Z[z, data.tokens(n)[i]] + beta) /
						((double)countZ[z] + data.wordNum * beta);
				if (z != 0)
				{
					prob[z] += prob[z - 1];
				}
			}
			double u = random.NextDouble() * prob[topicNum - 1];
			for (int z = 0; z < topicNum; z++)
			{
				if (u < prob[z])
				{
					return z;
				}
			}
			return topicNum - 1;
		}

		/// <summary>
		/// カウントを増やす
		/// </summary>
		/// <param name="n"></param>
		/// <param name="i"></param>
		private void addSample(int n, int i)
		{
			countW_Z[assign[n][i], data.tokens(n)[i]]++;
			countZ_D[n, assign[n][i]]++;
			countZ[assign[n][i]]++;
		}

		/// <summary>
		/// カウントを減らす
		/// </summary>
		/// <param name="n"></param>
		/// <param name="i"></param>
		private void removeSample(int n, int i)
		{
			countW_Z[assign[n][i], data.tokens(n)[i]]--;
			countZ_D[n, assign[n][i]]--;
			countZ[assign[n][i]]--;
		}

		/// <summary>
		/// Thetaを計算する
		/// </summary>
		/// <returns></returns>
		public double[][] getTheta()
		{
			double[][] theta = new double[data.docNum()][];
			for (int n = 0; n < data.docNum(); n++)
			{
				theta[n] = new double[topicNum];
				for (int z = 0; z < topicNum; z++)
				{
					theta[n][z] = countZ_D[n, z] + alpha;
				}
				Normalization.normalize(theta[n]);
			}
			return theta;
		}

		/// <summary>
		/// Phiを計算する
		/// </summary>
		/// <returns></returns>
		public double[][] getPhi()
		{
			double[][] phi = new double[topicNum][];
			for (int z = 0; z < topicNum; z++)
			{
				phi[z] = new double[data.wordNum];
				for (int k = 0; k < data.wordNum; k++)
				{
					phi[z][k] = countW_Z[z, k] + beta;
				}
				Normalization.normalize(phi[z]);
			}
			return phi;
		}


		public double perplexity()
		{
			double[][] theta = getTheta();
			double[][] phi = getPhi();
			double logP = 0.0;
			for (int n = 0; n < data.docNum(); n++)
			{
				for (int i = 0; i < data.tokens(n).Length; i++)
				{
					int token = data.tokens(n)[i];
					double sum = 0.0;
					for (int z = 0; z < topicNum; z++)
					{
						sum += theta[n][z] * phi[z][token];
					}
					logP += Math.Log(sum);
				}
			}
			return Math.Exp(-logP / tokenNum);
		}

		/// <summary>
		/// 文章ごとのトピック出力確率とトピックごとの単語出力確率を出力する
		/// </summary>
		/// <param name="filename"></param>
		/// <param name="threshold"></param>
		public void output(String filename, double threshold)
		{
			using (StreamWriter sw = new System.IO.StreamWriter(filename + "_theta.csv", false, System.Text.Encoding.GetEncoding("shift_jis")))
			{
				double[][] theta = getTheta();
				for (int n = 0; n < data.docNum(); n++)
				{
					for (int z = 0; z < topicNum; z++)
					{
						if (theta[n][z] >= threshold)
							sw.WriteLine(n + "," + z + "," + theta[n][z]);
					}
				}
			}

			using (StreamWriter sw = new System.IO.StreamWriter(filename + "_phi.csv", false, System.Text.Encoding.GetEncoding("shift_jis")))
			{
				double[][] phi = getPhi();
				for (int z = 0; z < topicNum; z++)
				{
					for (int k = 0; k < data.wordNum; k++)
					{
						if (phi[z][k] >= threshold)
							sw.WriteLine(z + "," + k + "," + phi[z][k]);
					}
				}
			}
		}

		private static readonly DateTime Jan1st1970 = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

		/// <summary>
		/// javaのCurrentTimeMillis()を再現する
		/// </summary>
		/// <returns></returns>
		public static long CurrentTimeMillis()
		{
			return (long)(DateTime.UtcNow - Jan1st1970).TotalMilliseconds;
		}
	}
}
