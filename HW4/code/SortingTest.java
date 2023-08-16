import java.io.*;
import java.util.*;

public class SortingTest {
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			boolean isRandom = false; // 입력받은 배열이 난수인가 아닌가?
			int[] value; // 입력 받을 숫자들의 배열
			String nums = br.readLine(); // 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r') {
				// 난수일 경우
				isRandom = true; // 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]); // 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]); // 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]); // 최대값

				Random rand = new Random(); // 난수 인스턴스를 생성한다.

				value = new int[numsize]; // 배열을 생성한다.
				for (int i = 0; i < value.length; i++) // 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			} else {
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize]; // 배열을 생성한다.
				for (int i = 0; i < value.length; i++) // 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true) {
				int[] newvalue = (int[]) value.clone(); // 원래 값의 보호를 위해 복사본을 생성한다.
				char algo = ' ';

				if (args.length == 4) {
					return;
				}

				String command = args.length > 0 ? args[0] : br.readLine();

				if (args.length > 0) {
					args = new String[4];
				}

				long t = System.currentTimeMillis();
				switch (command.charAt(0)) {
					case 'B': // Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I': // Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H': // Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M': // Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q': // Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R': // Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S': // Search
						algo = DoSearch(newvalue);
						break;
					case 'X':
						return; // 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom) {
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");
					if (command.charAt(0) == 'S')
						System.out.println(algo);
				} else {
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					if (command.charAt(0) != 'S') {
						for (int i = 0; i < newvalue.length; i++) {
							System.out.println(newvalue[i]);
						}
					} else {
						System.out.println(algo);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	static int[] DoBubbleSort(int[] value) { // did by chatGPT
		int n = value.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (value[j] > value[j + 1]) {
					int temp = value[j];
					value[j] = value[j + 1];
					value[j + 1] = temp;
				}
			}
		}
		return (value);
	}

	public static int[] DoInsertionSort(int[] value) {// did by chatGPT
		int n = value.length;
		for (int i = 1; i < n; ++i) {
			int key = value[i];
			int j = i - 1;
			while (j >= 0 && value[j] > key) {
				value[j + 1] = value[j];
				j = j - 1;
			}
			value[j + 1] = key;
		}
		return value;
	}

	public static int[] DoHeapSort(int[] value) {// did by chatGPT
		int n = value.length;

		// Build heap (rearrange array)
		for (int i = n / 2 - 1; i >= 0; i--) {
			makeHeap(value, n, i);
		}

		// One by one extract an element from heap
		for (int i = n - 1; i > 0; i--) {
			// Move current root to end
			int temp = value[0];
			value[0] = value[i];
			value[i] = temp;

			// call max makeHeap on the reduced heap
			makeHeap(value, i, 0);
		}
		return value;
	}

	// To makeHeap a subtree rooted with node i which is
	// an index in arr[]. n is size of heap
	static void makeHeap(int[] value, int n, int i) {// did by chatGPT
		int largest = i; // Initialize largest as root
		int l = 2 * i + 1; // left = 2*i + 1
		int r = 2 * i + 2; // right = 2*i + 2

		// If left child is larger than root
		if (l < n && value[l] > value[largest]) {
			largest = l;
		}

		// If right child is larger than largest so far
		if (r < n && value[r] > value[largest]) {
			largest = r;
		}

		// If largest is not root
		if (largest != i) {
			int swap = value[i];
			value[i] = value[largest];
			value[largest] = swap;

			// Recursively makeHeap the affected sub-tree
			makeHeap(value, n, largest);
		}
	}

	public static int[] DoMergeSort(int[] value) {// did by chatGPT
		int n = value.length;
		if (n <= 1) {
			return value;
		}
		int mid = n / 2;
		int[] left = new int[mid];
		int[] right = new int[n - mid];
		for (int i = 0; i < mid; i++) {
			left[i] = value[i];
		}
		for (int i = mid; i < n; i++) {
			right[i - mid] = value[i];
		}
		left = DoMergeSort(left);
		right = DoMergeSort(right);
		return merge(left, right);
	}

	static int[] merge(int[] left, int[] right) {// did by chatGPT
		int[] result = new int[left.length + right.length];
		int i = 0, j = 0, k = 0;
		while (i < left.length && j < right.length) {
			if (left[i] <= right[j]) {
				result[k++] = left[i++];
			} else {
				result[k++] = right[j++];
			}
		}
		while (i < left.length) {
			result[k++] = left[i++];
		}
		while (j < right.length) {
			result[k++] = right[j++];
		}
		return result;
	}

	public static int[] DoQuickSort(int[] value) {
		int low = 0;
		int high = value.length - 1;
		quickSort(value, low, high);
		return value;
	}

	static void quickSort(int[] value, int low, int high) {// did by chatGPT
		if (low < high) {
			int partitionIndex = partition(value, low, high);
			quickSort(value, low, partitionIndex - 1);
			quickSort(value, partitionIndex + 1, high);
		}
	}
	
	static int partition(int[] value, int low, int high) {// did by chatGPT
		int pivot = value[high];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (value[j] < pivot || (value[j] == pivot && j % 2 == 0)) {
				i++;
				int temp = value[i];
				value[i] = value[j];
				value[j] = temp;
			}
		}
		int temp = value[i + 1];
		value[i + 1] = value[high];
		value[high] = temp;
		return i + 1;
	}

	public static int[] DoRadixSort(int[] value) {// did by chatGPT
		int max_num = Integer.MIN_VALUE;
		for (int i = 0; i < value.length; i++) {
			max_num = Math.max(max_num, Math.abs(value[i]));
		}
		for (int exp = 1; max_num / exp > 0; exp *= 10) {
			countingSort(value, exp);
		}
		return value;
	}

	private static void countingSort(int[] array, int exp) {// did by chatGPT
		int n = array.length;
		int[] output = new int[n];
		int[] count = new int[20];
		Arrays.fill(count, 0);
		for (int i = 0; i < n; i++) {
			count[((array[i] / exp) % 10 + 9)]++;
		}

		for (int i = 1; i < 20; i++) {
			count[i] += count[i - 1];
		}

		for (int i = n - 1; i >= 0; i--) {
			output[count[((array[i] / exp) % 10 + 9)] - 1] = array[i];
			count[((array[i] / exp) % 10 + 9)]--;
		}

		System.arraycopy(output, 0, array, 0, n);
	}

	static char DoSearch(int[] value) {
		int decrease_count = -1; // number of max increasing consecutive subseq
		int increase_count = -1; // number of max decreasing consecurive subseq
		long square_time = 0; // square_time complexity used for quicksort
		int max_term = value[0]; // maximum term
		int min_term = value[0]; // minimum term
		HashMap<Integer, Integer> map = new HashMap<>(); // hashmap for <value, number of values>
		for (int i = 0; i < value.length - 1; i++) {
			if (decrease_count == -1 && value[i] > value[i + 1])
				decrease_count = value.length - i; // count number of max increasing consecutive subseq.
			if (increase_count == -1 && value[i] < value[i + 1])
				increase_count = i;
			max_term = Math.max(max_term, value[i + 1]); // update max_term
			min_term = Math.min(min_term, value[i + 1]); // update min_term
			if (!map.containsKey(value[i])) // alter value of hashmap
				map.put(value[i], 1);
			else
				map.put(value[i], map.get(value[i]) + 1);
		}
		if (decrease_count < (int) Math.log(value.length)) // if the given array is already almost sorted, use
															// insertionsort
			return 'I';
		else if (increase_count <= (int) Math.log(value.length)) // if the given array is already almost sorted
																	// backwards, use heapsort
			return 'H';
		if (max_term < 100 && min_term > -100)
			return 'R'; // if values are sufficiently small, use radixsort
		for (int key : map.keySet()) {
			square_time += map.get(key) * map.get(key);
		}
		if (square_time >= value.length * (int) Math.log(value.length) * 10) // if square_time is sufficiently large,
																				// use heapsort
			return 'H';
		return 'Q'; // use quicksort for default
	}
}
