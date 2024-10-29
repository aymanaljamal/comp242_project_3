package application;

import java.util.Arrays;

public class MartyrHash<T extends Comparable<T>> {
  

    private HNode<T>[] hashTable;
    private int size;
    private int elementCount;

    public MartyrHash(int n) {
        this.size = nextPrime(n * 2);
        this.hashTable = (HNode<T>[]) new HNode[this.size];
        this.elementCount = 0;
    }

    public void add(T data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null && hashTable[index].getFlag() == 'F'
                && !hashTable[index].getData().equals(data)) {
            index = probe(index, ++i);
        }

        if (hashTable[index] == null || hashTable[index].getFlag() == 'E' || hashTable[index].getFlag() == 'D') {
            hashTable[index] = new HNode<>(data);
            hashTable[index].setFlag('F');
            elementCount++;
            if ((double) elementCount / hashTable.length >= 0.7) {
                rehash();
            }
        }
    }

    public T find(T data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() =='F' && hashTable[index].getData().equals(data)) {
                return hashTable[index].getData();
            }
            index = probe(index, ++i);
        }
        return null;
    }

    public boolean delete(T data) {
        int index = hash(data);
        int i = 0;

        while (hashTable[index] != null) {
            if (hashTable[index].getFlag() == 'F' && hashTable[index].getData().equals(data)) {
                hashTable[index].setFlag('D');
                hashTable[index].setData(null);
                return true;
            }
            index = probe(index, ++i);
        }
        return false;
    }

    private void rehash() {
        HNode<T>[] oldTable = hashTable;
        int newSize = nextPrime(2 * oldTable.length);
        hashTable = (HNode<T>[]) new HNode[newSize];
        size = newSize;
        elementCount = 0;

        for (HNode<T> node : oldTable) {
            if (node != null && node.getFlag() =='F') {
                add(node.getData());
            }
        }
    }

    private int nextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int hash(T data) {
        return Math.abs(data.hashCode() % size);
    }

    private int probe(int hash, int i) {
        return (hash + i * i) % size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "HashTable [hashTable=" + Arrays.toString(hashTable) + "]";
    }

    public void traverse() {
        for (HNode<T> node : hashTable) {
            if (node != null && node.getFlag() == 'F') {
                System.out.println(node.getData());
            }
        }
    }

    public HNode<T>[] getHashTable() {
        return hashTable;
    }

    public void setHashTable(HNode<T>[] hashTable) {
        this.hashTable = hashTable;
    }
}
