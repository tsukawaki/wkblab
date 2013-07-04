using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LDA
{
	class Program
	{
		static void Main(string[] args)
		{
			int iteration = 100;
			LDA lda = LDA.getInstance("1000set.txt", 20, 0.01, 0.01, 0);
			lda.sampling(iteration);
			lda.output("LDAresult", 1.0E-5);
			Console.WriteLine("iteration "+iteration+"\tperplexity:"+lda.perplexity());
		}
	}
}
