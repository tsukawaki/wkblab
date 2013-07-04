using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace LDA
{
	class TokenData
	{
		/// <summary>
		/// 文章ごとの文字列
		/// </summary>
		private List<int[]> data;
		/// <summary>
		/// 単語数
		/// </summary>
		public int wordNum { get; set; }

		/// <summary>
		/// 文章データを読み込んで、トークンデータを返す
		/// </summary>
		/// <param name="filename"></param>
		/// <returns></returns>
		static public TokenData loadData(String filename)
		{
			TokenData td = new TokenData();
			td.data = new List<int[]>();
			HashSet<int> wordSet = new HashSet<int>();
			using (StreamReader sr = new StreamReader(filename, Encoding.GetEncoding("Shift_JIS")))
			{
				string line;
				while ((line = sr.ReadLine()) != null)
				{
					string[] strList = line.Split(' ');
					int[] intList = new int[strList.Length];
					for (int i = 0; i < strList.Length; i++)
					{
						int wordID = int.Parse(strList[i]);
						intList[i] = wordID;
						wordSet.Add(wordID);
					}
					td.data.Add(intList);
				}
			}
			td.wordNum = wordSet.Count();
			return td;
		}

		/// <summary>
		/// 指定した文章番号の文字列を返す
		/// </summary>
		/// <param name="docID"></param>
		/// <returns></returns>
		public int[] tokens(int docID)
		{
			return data[docID];
		}

		/// <summary>
		/// 文章数を返す
		/// </summary>
		/// <returns></returns>
		public int docNum()
		{
			return data.Count();
		}
	}
}
