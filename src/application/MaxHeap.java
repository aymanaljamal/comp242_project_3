package application;



public class MaxHeap<T extends Comparable<T>> implements MaxHeapInterface<T> {

	private T[] arr;
	private int N;
	
	
	public MaxHeap(int size) {
		arr = (T[]) new Comparable[size + 1];
	}
	
	@Override
	public void add(T data) {
		arr[++N] = data;
		swim(N);
	}

	@Override
	public T removeMax() {
		T max = arr[1];
		swap(1, N--);
		sink(1);
		arr[N+1] = null; 
		return max;
	}

	@Override
	public T getMax() {
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
		while (k > 1 && less(k/2, k)) {
			swap(k/2, k);
			k /= 2;
		}
	}
	
	public T[] getArr() {
		return arr;
	}

	public void setArr(T[] arr) {
		this.arr = arr;
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
			if (j < N && less(j, j+1)) j++;
			if (!less(k, j)) break;
			swap(k, j);
			k = j;
		}
	}
	
	public static boolean isMaxHeap(Comparable[] a) {
		int N = a.length;
		for (int i = 1; i <= N/2; i++) {
			int lc = i*2, rc = lc + 1;
			if (lc < N && a[i].compareTo(a[lc]) < 0) return false;
			if (rc < N && a[i].compareTo(a[rc]) < 0) return false;
		}
		return true;
	}
	
	

	    public static void heapSortDsc(Comparable[] a) {
	        int N = a.length - 1;
	        Comparable temp;

	        maxHeapify(a);

	        while (N > 0) {
	           
	            temp = a[0];
	            a[0] = a[N];
	            a[N] = temp;

	            N--;

	         
	            int k = 0;
	            while (2 * k + 1 <= N) {
	                int j = 2 * k + 1;
	                if (j < N && a[j].compareTo(a[j + 1]) < 0) j++;
	                if (a[k].compareTo(a[j]) >= 0) break;
	                temp = a[k];
	                a[k] = a[j];
	                a[j] = temp;
	                k = j;
	            }
	        }
	    }

	    public static void maxHeapify(Comparable[] a) {
	        int N = a.length;
	        for (int i = N / 2 - 1; i >= 0; i--) {
	            heapify(a, i, N);
	        }
	    }

	    private static void heapify(Comparable[] a, int i, int N) {
	        int largest = i;
	        int left = 2 * i + 1;
	        int right = 2 * i + 2;

	       
	        if (left < N && a[left].compareTo(a[largest]) > 0) {
	            largest = left;
	        }
	        if (right < N && a[right].compareTo(a[largest]) > 0) {
	            largest = right;
	        }

	        
	        if (largest != i) {
	            Comparable temp = a[i];
	            a[i] = a[largest];
	            a[largest] = temp;

	            heapify(a, largest, N);
	        }
	    }
	}

	
