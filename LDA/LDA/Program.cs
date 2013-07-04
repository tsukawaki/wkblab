using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace LDA
{
	class Program
	{
		static void Main(string[] args)
		{
            List<string> files = new List<string>();
            for (int d = 1; d < 22; d++)
            {
                files.Add("testdata/test" + d + ".txt");
            }
            
            // 文章を読み込む
            string line = "";
            List<string> doc = new List<string>();
            foreach (string filename in files)
            {
                using (StreamReader sr = new StreamReader(filename, Encoding.GetEncoding("Shift_JIS")))
                {
                    string lines = "";
                    while ((line = sr.ReadLine()) != null)
                    {
                        lines += line;

                    }
                    doc.Add(lines);
                }
            }

            // 文章を数値に変換する
            ConvartDoc convarter = new ConvartDoc();
            List<int[]> On = convarter.tryConvart(doc);
            string[] strDic = convarter.strDic.ToArray();// 数字と単語の対応表

            ///LDA-----------------------------------
            
			int iteration = 1000;
            int topicNum = 20;
            double threshold = 1.0E-5;
			HMM lda = HMM.getInstance(On, strDic.Length, topicNum, 0.01, 0.01, 0);
			lda.sampling(iteration);
			lda.output("LDAresult", threshold);
			Console.WriteLine("iteration "+iteration+"\tperplexity:"+lda.perplexity());

            // トピック割り当ての出力
            using (StreamWriter sw = new System.IO.StreamWriter("HMMresult_assign.csv", false, System.Text.Encoding.GetEncoding("shift_jis")))
            {
                sw.WriteLine("トピック,Phi,単語");
                double[][] phi = lda.getPhi();
                int[][] assign = lda.assign;
                for (int d = 0; d < assign.GetLength(0); d++) {
                    for (int t = 0; t < On[d].Length; t++)
                    {
                        sw.WriteLine(assign[d][t] + "," + phi[assign[d][t]][On[d][t]] + "," + strDic[On[d][t]]);
                    }    
                }
            }
		}
	}
}
