package edu.wit.cs.comp3370;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/* Adds floating point numbers with varying precision 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 2
 * 
 */

public class LAB2 {

	/**
 	 * Creates a sorted heap out of the input array
 	 * and adds the floats smallest at a time.
 	 * This is a quick way and more accurate way add a list of floats
 	 *
 	 */
	public static float heapAdd(float[] a) {
		if (a.length == 0) {
			return 0;
		}
		float[] heap = new float[a.length];

		// Make heap
		int end = a.length - 1;
		for (int x = 0; x < a.length; x++) {
			heap[x] = a[x];
			pullup(heap, x);
		}

		// loop and add
		while (end > 0) {
			// Take the minimum off the head
			// move the end to the head
			float min = heap[0];
			heap[0] = heap[end];
			// Make the heap smaller, remaking arrays is slow 
			// Reorganize to make sure the heap is still sorted
			end--; 
			pushdown(heap, 0, end);
			// add the min we pulled off
			// reorganize again to keep the heap sorted
			heap[0] += min;
			pushdown(heap, 0, end);
		}

		return heap[0];

	}

	private static void pushdown(float[] a, int i, int max) {
		if (i >= max) {
			return;
		}
		int mini = i;
		int left = getLeftChild(i);
		int right = getRightChild(i);

		// <= since max is the largest index not the array length
		if (left <= max && a[i] > a[left]) {
			mini = left;
		}
		if (right <= max && a[mini] > a[right]) {
			mini = right;
		}

		if (mini != i) {
			float t = a[i];
			a[i] = a[mini];
			a[mini] = t;

			pushdown(a, mini, max);
		}

	}

	private static void pullup(float[] a, int i) {
		if (i == 0) {
			return;
		}
		int p = getParent(i);
		if (a[i] < a[p]) {
			float temp = a[i];
			a[i] = a[p];
			a[p] = temp;
			pullup(a, p);
		}
	}

	private static int getParent(int i) {
		return (i - 1) / 2;
	}

	private static int getLeftChild(int i) {
		return 2 * i + 1;
	}

	private static int getRightChild(int i) {
		return 2 * i + 2;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially - high rounding error
	public static float seqAdd(float[] a) {
		float ret = 0;

		for (int i = 0; i < a.length; i++)
			ret += a[i];

		return ret;
	}

	// sort an array of floats and then sum sequentially - medium rounding error
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	// minimized rounding error
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;

		if (a.length == 0)
			return 0;

		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {

			if (a[0] < a[1]) {
				min1 = 0;
				min2 = 1;
			} // initialize
			else {
				min1 = 1;
				min2 = 0;
			}

			for (int j = 2; j < end; j++) { // find two min indices
				if (a[min1] > a[j]) {
					min2 = min1;
					min1 = j;
				} else if (a[min2] > a[j]) {
					min2 = j;
				}
			}

			tmp = a[min1] + a[min2]; // add together
			if (min1 < min2) { // put into first slot of array
				a[min1] = tmp; // fill second slot from end of array
				a[min2] = a[end - 1];
			} else {
				a[min2] = tmp;
				a[min1] = a[end - 1];
			}
		}

		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
		char algo = s.next().charAt(0);

		System.out
				.printf("Enter the non-negative floats that you would like summed, followed by a non-numeric input: ");
		float[] values = getFloats(s);
		float sum = 0;

		s.close();

		if (values.length == 0) {
			System.out.println("You must enter at least one value");
			System.exit(0);
		} else if (values.length == 1) {
			System.out.println("Sum is " + values[0]);
			System.exit(0);

		}

		switch (algo) {
		case 'h':
			sum = heapAdd(values);
			break;
		case 'm':
			sum = min2ScanAdd(values);
			break;
		case 'q':
			sum = seqAdd(values);
			break;
		case 's':
			sum = sortAdd(values);
			break;
		default:
			System.out.println("Invalid adding algorithm");
			System.exit(0);
			break;
		}

		System.out.printf("Sum is %f\n", sum);

	}

}
