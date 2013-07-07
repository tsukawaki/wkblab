using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NMeCab;

namespace LDA
{
    class ConvartDoc
    {
        public List<string> strDic;// 出現する単語の配列

        public ConvartDoc()
        {
            strDic = new List<string>();
        }

        /// <summary>
        /// 文章を読み込んで数値に変換したものを返す
        /// </summary>
        /// 
        public List<int[]> tryConvart(List<string> doc)
        {
            List<int[]> list = new List<int[]>();

            // MeCab::Tagger
            MeCabTagger tagger = MeCabTagger.Create();

            foreach (string str in doc)
            {
                List<int> words = new List<int>();

                MeCabNode node = tagger.ParseToNode(str);

                while (node != null)
                {
                    if (node.Feature.Contains("BOS"))
                    {
                    //if ((!node.Feature.Contains("名詞") && node.Surface != "。" && node.Surface != "?") || node.Feature.Contains("数"))
                    //{
                        node = node.Next;
                        continue;
                    }
                    var word = node.Surface;
                    // strDicに既に含まれて入れば、インデックスの番号に変換する
                    if (!strDic.Contains(word))
                    {
                        strDic.Add(word);
                    }
                    words.Add(strDic.IndexOf(word));
                    node = node.Next;
                }

                list.Add(words.ToArray());
            }

            return list;
        }

        static public void tryParse()
        {
            string str = "こいつにスパゲティを食わしてやりたいんですが、かまいませんね！！";

            // MeCab::Tagger
            MeCabTagger tagger = MeCabTagger.Create();

            // MeCab::Node
            MeCabNode node = tagger.ParseToNode(str);

            // 形態素を１つずつ取り出す
            while (node != null)
            {
                Console.WriteLine(node.Surface + " : " + node.Feature);
                node = node.Next;
            }
            Console.WriteLine();
        }
    }
}
