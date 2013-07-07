using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LDA
{
	class Normalization
	{
		/// <summary>
		/// 配列内のdoubleの数値をノーマライズする(参照渡し用？)
		/// </summary>
		/// <param name="vector"></param>
		static public void normalize(double[] vector)
		{
			normalize(vector, calcSum(vector));
		}

		static public void normalize(double[] vector, double sum)
		{
			double div = 1.0 / sum;
			for (int i = 0; i < vector.Length; i++)
			{
				vector[i] *= div;
			}
		}

		/// <summary>
		/// 配列内のdoubleの数値をノーマライズする（値渡し用）
		/// </summary>
		/// <param name="vector"></param>
		/// <returns></returns>
		static public double[] getNormalized(double[] vector)
		{
			return getNormalized(vector, calcSum(vector));
		}

		static public double[] getNormalized(double[] vector, double sum)
		{
			double[] normalized = new double[vector.Length];
			double div = 1.0 / sum;
			for (int i = 0; i < vector.Length; i++)
			{
				normalized[i] = vector[i] * div;
			}
			return normalized;
		}

		static public double calcSum(double[] vector)
		{
			double sum = 0.0;
			for (int i = 0; i < vector.Length; i++)
			{
				sum += vector[i];
			}
			return sum;
		}
	}
}
