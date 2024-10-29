package application;

import javafx.collections.ObservableList;

public class AVLTree<A extends Comparable<A>> {
	private TNode<A> root;
	private Stack<A> nextStack = new Stack<>();
	private Stack<A> prefStack = new Stack<>();
	public TNode<A> getRoot() {
		return root;
	}

	public void setRoot(TNode<A> root) {
		this.root = root;
	}

	private int getHeight(TNode<A> N) {
		if (N == null)
			return 0;
		return 1 + Math.max(getHeight(N.getLeft()), getHeight(N.getRight()));
	}

	private int getBalance(TNode<A> N) {
		if (N == null)
			return 0;
		return getHeight(N.getLeft()) - getHeight(N.getRight());
	}

	private TNode<A> rightRotate(TNode<A> y) {
		TNode<A> x = y.getLeft();
		TNode<A> T2 = x.getRight();
		x.setRight(y);
		y.setLeft(T2);
		return x;
	}

	private TNode<A> leftRotate(TNode<A> x) {
		TNode<A> y = x.getRight();
		TNode<A> T2 = y.getLeft();
		y.setLeft(x);
		x.setRight(T2);
		return y;
	}

	public TNode<A> insert(TNode<A> node, A key) {
		if (node == null) {
			return new TNode<>(key);
		}

		if (key.compareTo(node.getData()) < 0) {
			node.setLeft(insert(node.getLeft(), key));
		} else if (key.compareTo(node.getData()) > 0) {
			node.setRight(insert(node.getRight(), key));
		} else {
			return node;
		}

		return rebalance(node);
	}

	public void insert(A key) {
		root = insert(root, key);
	}

	public TNode<A> deleteNode(TNode<A> root, A key) {
		if (root == null)
			return null;

		if (key.compareTo(root.getData()) < 0) {
			root.setLeft(deleteNode(root.getLeft(), key));
		} else if (key.compareTo(root.getData()) > 0) {
			root.setRight(deleteNode(root.getRight(), key));
		} else {
			if (root.getLeft() == null || root.getRight() == null) {
				root = (root.getLeft() == null) ? root.getRight() : root.getLeft();
			} else {
				TNode<A> temp = minValueNode(root.getRight());
				root.setData(temp.getData());
				root.setRight(deleteNode(root.getRight(), temp.getData()));
			}
		}

		return root == null ? null : rebalance(root);
	}

	public boolean delete(A key) {
		if (find(key) == null)
			return false;
		root = deleteNode(root, key);
		return find(key) == null;
	}

	private TNode<A> minValueNode(TNode<A> node) {
		TNode<A> current = node;
		while (current.getLeft() != null) {
			current = current.getLeft();
		}
		return current;
	}

	private TNode<A> rebalance(TNode<A> node) {
		int balance = getBalance(node);
		if (balance > 1) {
			if (getBalance(node.getLeft()) < 0) {
				node.setLeft(leftRotate(node.getLeft()));
			}
			return rightRotate(node);
		} else if (balance < -1) {
			if (getBalance(node.getRight()) > 0) {
				node.setRight(rightRotate(node.getRight()));
			}
			return leftRotate(node);
		}
		return node;
	}

	public void preOrder(TNode<A> node) {
		if (node != null) {
			System.out.print(node.getData() + " ");
			preOrder(node.getLeft());
			preOrder(node.getRight());
		}
	}

	public void preOrder() {
		preOrder(root);
	}

	public void preOrder(TNode<A> node, StringBuilder sb) {
		if (node != null) {
			sb.append(node.getData()).append(" ");
			preOrder(node.getLeft(), sb);
			preOrder(node.getRight(), sb);
		}
	}

	public String printTree() {
		StringBuilder sb = new StringBuilder();
		preOrder(root, sb);
		return sb.toString();
	}

	public TNode<A> find(A data) {
		return find(data, root);
	}

	private TNode<A> find(A data, TNode<A> node) {
		if (node == null) {
			return null;
		}
		if (node.getData().equals(data)) {
			return node;
		}

		TNode<A> leftResult = find(data, node.getLeft());
		if (leftResult != null) {
			return leftResult;
		}
		return find(data, node.getRight());
	}
	public A getNextlevl() {
		if (nextStack.isEmpty()) {
			return null;
		}
		A data = nextStack.pop();
		prefStack.push(data);
		return data;
	}

	public A getprevlevl() {
		if (prefStack.isEmpty()) {
			return null;
		}
		A data = prefStack.pop();
		nextStack.push(data);
		return data;
	}

	public void StackFillingLevelByLevel() {
	    nextStack.clear();
	    prefStack.clear();
	    stackFillingLevelByLevel(root);
	}

	public void stackFillingLevelByLevel(TNode<A> rootNode) {
	    if (rootNode == null)
	        return;

	    LinkedListQueue<TNode<A>> queue = new LinkedListQueue<>();
	    queue.enqueue(rootNode);
	    Stack<A> tempStack = new Stack<>();

	    while (!queue.isEmpty()) {
	        TNode<A> currentNode = queue.dequeue();

	        if (currentNode.getLeft() != null) {
	            queue.enqueue(currentNode.getLeft());
	        }
	        if (currentNode.getRight() != null) {
	            queue.enqueue(currentNode.getRight());
	        }

	        tempStack.push(currentNode.getData());
	    }

	    while (!tempStack.isEmpty()) {
	        nextStack.push(tempStack.pop());
	    }
	}

	public void populateLevelOrderRightToLeft(ObservableList<A> list) {
        if (root == null) {
            return;
        }

        LinkedListQueue<TNode<A>> queue = new LinkedListQueue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TNode currentNode = queue.dequeue();
                list.add((A) currentNode.data);

                if (currentNode.right != null) {
                    queue.enqueue(currentNode.right);
                }
                if (currentNode.left != null) {
                    queue.enqueue(currentNode.left);
                }
            
        }}}
	public boolean isEmpty() {
		return root == null;
	}

	public int height() {
		return height(root);
	}

	private int height(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 1;
		int left = 0;
		int right = 0;
		if (node.hasLeft())
			left = height(node.getLeft());
		if (node.hasRight())
			right = height(node.getRight());
		return (left > right) ? (left + 1) : (right + 1);
	}
	public int countLeaves(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 1;
		return countLeaves(node.getLeft()) + countLeaves(node.getRight());
	}

	public int countParents(TNode<A> node) {
		if (node == null)
			return 0;
		if (node.isLeaf())
			return 0;
		return 1 + countParents(node.getLeft()) + countParents(node.getRight());
	}

	int size() {
		return countLeaves(root) + countParents(root);
	}
}
