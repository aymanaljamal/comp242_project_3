package application;



public class MinHeap<T extends Comparable<T>> implements MinHeapInterface<T> {

	private T[] arr;
	private int N;
	

	public MinHeap(int size) {
		arr = (T[]) new Comparable[size + 1];
	}
	
	@Override
	public void add(T data) {
		arr[++N] = data;
		swim(N);
	}

	@Override
	public T removeMin() {
		T min = arr[1];
		swap(1, N--);
		sink(1);
		arr[N+1] = null; 
		return min;
	}

	@Override
	public T getMin() {
		return arr[1];
	}

	@Override
	public boolean isEmpty() {
		return N == 0;
	}

	@Override
	public int getSize() {
		return N;
	}

	@Override
	public void clear() {
		N = 0;
	}
	
	private void swim(int k) {
		while (k > 1 && !less(k/2, k)) {
			swap(k/2, k);
			k /= 2;
		}
	}
	
	private boolean less(int p, int c) {
		return arr[p].compareTo(arr[c]) < 0;
	}
	
	private void swap(int p, int c) {
		T temp = arr[p];
		arr[p] = arr[c];
		arr[c] = temp;
	}
	
	private void sink(int k) {
		while (2*k <= N) {
			int j = 2*k;
			if (j < N && !less(j, j+1)) j++;
			if (less(k, j)) break;
			swap(k, j);
			k = j;
		}
	}
	
	public static boolean isMinHeap(Comparable[] a) {
		int N = a.length;
		for (int i = 1; i <= N/2; i++) {
			int lc = i*2, rc = lc + 1;
			if (lc < N && a[i].compareTo(a[lc]) > 0) return false;
			if (rc < N && a[i].compareTo(a[rc]) > 0) return false;
		}
		return true;
	}
	
	   public static void heapSortAesc(Comparable[] a) {
	        int N = a.length - 1;
	        Comparable temp;

	        minHeapify(a);  

	        while (N > 0) {
	            
	            temp = a[0];
	            a[0] = a[N];
	            a[N] = temp;

	            N--; 

	            int k = 0;
	            while (2 * k + 1 <= N) {
	                int j = 2 * k + 1;
	                if (j < N && a[j].compareTo(a[j + 1]) >= 0) j++;
	                if (a[k].compareTo(a[j]) <= 0) break;
	                temp = a[k];
	                a[k] = a[j];
	                a[j] = temp;
	                k = j;
	            }
	        }
	    }

	    public static void minHeapify(Comparable[] a) {
	        int N = a.length;
	        for (int i = N / 2 - 1; i >= 0; i--) {
	            int k = i;
	            while (2 * k + 1 < N) {
	                int j = 2 * k + 1;
	                if (j + 1 < N && a[j].compareTo(a[j + 1]) >= 0) j++;
	                if (a[k].compareTo(a[j]) <= 0) break;
	                Comparable temp = a[k];
	                a[k] = a[j];
	                a[j] = temp;
	                k = j;
	            }
	        }
	    }


}