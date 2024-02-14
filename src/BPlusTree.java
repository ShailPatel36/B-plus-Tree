import java.util.ArrayList;

public class BPlusTree<K extends Comparable<K>, V> {

	public Node<K, V> root;
	public static final int ORDER = 2;

	private int fusionCnt = 0;
	private int parentFusionCnt = 0;
	private int splitCnt = 0;
	private int parentSplitCnt = 0;
	private int dataCnt = 0;

	public void showStats() {
		System.out.println("\n--- STATS ---");
		System.out.println("Tree Depth: " + getDepth());
		System.out.println("Fusion Count: " + fusionCnt);
		System.out.println("Parent Fusion Count: " + parentFusionCnt);
		System.out.println("Split Count: " + splitCnt);
		System.out.println("Parent Split Count: " + parentSplitCnt);
		System.out.println("Number of Keys Stored: " + dataCnt);
		System.out.println("-------------\n");
	}

	private int getDepth() {
		return treeDepth(root);
	}

	private int treeDepth(Node<K, V> node) {
		if (node.isLeaf())
			return 1;
		int childDepth = 0;
		int maxDepth = 0;
		for (Node<K, V> child : ((IntermediateNode<K, V>) node).children) {
			childDepth = treeDepth(child);
			if (childDepth > maxDepth)
				maxDepth = childDepth;
		}
		return (1 + maxDepth);
	}

	public ArrayList<Pair<K, V>> toArrayList() {
		ArrayList<Pair<K, V>> data = new ArrayList<>();

		Node<K, V> node = root;

		while (!node.isLeaf()) {
			IntermediateNode<K, V> interNode = (IntermediateNode<K, V>) node;
			node = interNode.getFirstChild();
		}

		LeafNode<K, V> leafNode;
		while (node != null) {
			leafNode = (LeafNode<K, V>) node;
			for (int i = 0; i < leafNode.getKeysSize(); i++) {
				data.add(new Pair<K, V>(leafNode.getKey(i), leafNode.getValue(i)));
			}
			node = leafNode.next;
		}
		return data;
	}

	public LeafNode<K, V> find(K key) {
		return (LeafNode<K, V>) treeSearch(root, key);
	}

	public V search(K key) {
		LeafNode<K, V> leaf = find(key);

		if (leaf == null) {
			return null;
		}

		return leaf.getValue(key);
	}

	public boolean update(K key, V val) {
		LeafNode<K, V> node = find(key);
		if (node == null)
			return false;

		for (int i = 0; i < node.getValuesSize(); i++) {
			K k = node.getKey(i);
			if (key.compareTo(k) == 0) {
				System.out.println("Updated Data for Key: " + key);
				System.out.println(node.getValue(i) + " --> " + val);
				node.values.set(i, val);
				return true;
			}
		}
		return false;
	}

	private Node<K, V> treeSearch(Node<K, V> node, K key) {
		if (node == null || key == null) return null;
		
		if(node.isLeaf())
    		return node;
    	
    	IntermediateNode<K,V> interNode = (IntermediateNode<K,V>) node;
    	
    	if(key.compareTo(interNode.getFirstKey()) < 0) 
    		return treeSearch(interNode.getFirstChild(), key);
    	
    	if(key.compareTo(interNode.getLastKey()) >= 0)
    		return treeSearch(interNode.getLastChild(), key);
    	
    	for(int i = 1; i < interNode.keys.size(); i++)
    		if(key.compareTo(interNode.getKey(i - 1)) >= 0)
    			if (key.compareTo(interNode.getKey(i)) < 0)
    				return treeSearch(interNode.getChild(i), key);
    	
    	return null;
	}

	public void insert(K key, V value) {
		Pair<K, Node<K, V>> pair = new Pair<>(key, new LeafNode<>(key, value));

		if (root == null || root.getKeysSize() == 0) {
			root = pair.getValue();
		}

		Pair<K, Node<K, V>> newChildPair = insertHelper(root, pair, null);

		if (newChildPair != null)
			root = new IntermediateNode<K, V>(newChildPair.getKey(), root, newChildPair.getValue());

		dataCnt++;
	}

	private Pair<K, Node<K, V>> insertHelper(Node<K, V> node, Pair<K, Node<K, V>> p, Pair<K, Node<K, V>> newChildPair) {
		if (node.isLeaf()) {
			LeafNode<K, V> leaf = (LeafNode<K, V>) node;
			LeafNode<K, V> newLeaf = (LeafNode<K, V>) p.getValue();

			leaf.insert(p.getKey(), newLeaf.getFirstValue());

			if (leaf.isOverFlow()) {
				newChildPair = splitNode(leaf);
				if (leaf == root) {
					root = new IntermediateNode<>(newChildPair.getKey(), leaf, newChildPair.getValue());
				} else {
					return newChildPair;
				}
			}
		} else {
			IntermediateNode<K, V> interNode = (IntermediateNode<K, V>) node;
			int idx1;
			for (idx1 = 0; idx1 < interNode.getKeysSize(); idx1++) {
				if (p.getKey().compareTo(interNode.getKey(idx1)) < 0) {
					break;
				}
			}

			newChildPair = insertHelper(interNode.getChild(idx1), p, newChildPair);
			if (newChildPair == null)
				return null;

			int idx2;
			for (idx2 = 0; idx2 < interNode.getKeysSize(); idx2++) {
				if (newChildPair.getKey().compareTo(interNode.getKey(idx2)) < 0) {
					break;
				}
			}

			interNode.insert(newChildPair, idx2);

			if (interNode.isOverFlow()) {
				newChildPair = splitNode(interNode);
				if (interNode == root) {
					root = new IntermediateNode<>(newChildPair.getKey(), root, newChildPair.getValue());
				} else {
					return newChildPair;
				}
			}
		}
		return null;
	}

	private Pair<K, Node<K, V>> splitNode(LeafNode<K, V> leaf) {
		ArrayList<K> newKeys = new ArrayList<>();
		ArrayList<V> newValues = new ArrayList<>();

		int currSize = leaf.getKeysSize();

		for (int i = ORDER; i <= 2 * ORDER; i++) {
			newKeys.add(leaf.getKey(ORDER));
			newValues.add(leaf.getValue(ORDER));
			leaf.keys.remove(ORDER);
			leaf.values.remove(ORDER);
		}

		LeafNode<K, V> rightLeaf = new LeafNode<>(newKeys, newValues);
		LeafNode<K, V> temp = leaf.next;
		leaf.next = rightLeaf;
		leaf.next.previous = rightLeaf;
		rightLeaf.previous = leaf;
		rightLeaf.next = temp;

		splitCnt++; // Inc Split Count

		return new Pair<>(rightLeaf.getFirstKey(), rightLeaf);
	}

	private Pair<K, Node<K, V>> splitNode(IntermediateNode<K, V> interNode) {
		ArrayList<K> newKeys = new ArrayList<>();
		ArrayList<Node<K, V>> newChildren = new ArrayList<>();

		K splitKey = interNode.getKey(ORDER);
		interNode.keys.remove(ORDER);

		newChildren.add(interNode.getChild(ORDER + 1));
		interNode.children.remove(ORDER + 1);

		int currSize = interNode.getKeysSize();

		for (int i = 0; i < (currSize - ORDER); i++) {
			newKeys.add(interNode.getKey(ORDER));
			interNode.keys.remove(ORDER);
			newChildren.add(interNode.getChild(ORDER + 1));
			interNode.children.remove(ORDER + 1);
		}

		parentSplitCnt++; // Inc Split Count

		return new Pair<>(splitKey, new IntermediateNode<>(newKeys, newChildren));
	}

	public boolean delete(K key) {
		LeafNode<K, V> leaf = (LeafNode<K, V>) find(key);
		if (leaf == null)
			return false;

		Pair<K, Node<K, V>> pair = deleteHelper(root, root, new Pair<>(key, leaf), null);

		if (pair == null) {
			if (root.getKeysSize() == 0) {
				if (!root.isLeaf()) {
					root = ((IntermediateNode<K, V>) root).getFirstChild();
				}
			}
			dataCnt--;
			return true;
		} else {
			int idx;
			for(idx = 0; idx < root.getKeysSize(); idx++) {
				if(pair.getKey().compareTo(root.getKey(idx)) == 0) {
					break;
				}
			}
			
			if(idx == root.getKeysSize()) {
				dataCnt--;
				return true;
			}
			
			root.keys.remove(idx);
			((IntermediateNode<K, V>) root).children.remove(idx + 1);
			dataCnt--;
			return true;
		}
	}

	private Pair<K, Node<K, V>> deleteHelper(Node<K, V> parentNode, Node<K, V> node, Pair<K, Node<K, V>> entry, Pair<K, Node<K, V>> oldChildEntry) {
		if (node.isLeaf()) {
			LeafNode<K, V> leaf = (LeafNode<K, V>) node;
			for (int idx = 0; idx < leaf.getKeysSize(); idx++) {
				if (leaf.getKey(idx).equals(entry.getKey())) {
					leaf.keys.remove(idx);
					leaf.values.remove(idx);
					break;
				}
			}

			if (leaf.isUnderFlow()) {
				if (leaf == root || leaf.getKeysSize() == 0) return oldChildEntry;
				int splitPos;
				if (leaf.previous != null && leaf.getFirstKey().compareTo(parentNode.getFirstKey()) >= 0) {
					splitPos = fuseNode(leaf.previous, leaf, (IntermediateNode<K, V>) parentNode);
				} else {
					splitPos = fuseNode(leaf, leaf.next, (IntermediateNode<K, V>) parentNode);
				}
				if (splitPos != -1) {
					return new Pair<>(parentNode.getKey(splitPos), parentNode);
				}
			}
		} else {
			IntermediateNode<K, V> interNode = (IntermediateNode<K, V>) node;
			int idx;
			for (idx = 0;idx < interNode.getKeysSize();idx++) {
				if (entry.getKey().compareTo(interNode.getKey(idx)) < 0) {
					break;
				}
			}
			oldChildEntry = deleteHelper(interNode, interNode.getChild(idx), entry, oldChildEntry);
			if (oldChildEntry != null) {
				// Remove the old child node from the intermediate node
				int childIdx = interNode.findChildIndex(oldChildEntry.getValue());
				interNode.keys.remove(childIdx);
				interNode.children.remove(childIdx + 1);

				if (interNode.isUnderFlow() && interNode.getKeysSize() > 0) {
					if (interNode == root) {
						return oldChildEntry;
					}
					for (idx=0; idx < parentNode.getKeysSize(); idx++) {
						if (interNode.getFirstKey().compareTo(parentNode.getKey(idx)) < 0) {
							break;
						}
					}
					int splitPos;
					IntermediateNode<K, V> parent = (IntermediateNode<K, V>) parentNode;
					if (idx > 0 && parent.getChild(idx - 1) != null) {
						splitPos = fuseNode((IntermediateNode<K, V>) parent.getChild(idx - 1), interNode, parent);
					} else {
						splitPos = fuseNode(interNode, (IntermediateNode<K, V>) parent.getChild(idx + 1), parent);
					}
					if (splitPos != -1) {
						return new Pair<>(parentNode.getKey(splitPos), parentNode);
					}
				}
			}
		}
		return null;
	}

	public int fuseNode(LeafNode<K, V> left, LeafNode<K, V> right, IntermediateNode<K, V> parent) {
		fusionCnt++;
		int idx;
		for (idx = 0; idx < parent.getKeysSize(); idx++) {
			if (right.getFirstKey().compareTo(parent.getKey(idx)) < 0) {
				break;
			}
		}
		if (left.getKeysSize() + right.getKeysSize() >= 2 * ORDER) {
			if (left.getKeysSize() > right.getKeysSize()) {
				for(int idx2 = 0; idx2 < (left.getKeysSize() - ORDER); idx2++) {
					right.keys.add(0, left.getLastKey());
					right.values.add(0, left.getLastValue());
					left.keys.remove(left.getKeysSize() - 1);
					left.values.remove(left.values.size() - 1);
				}
			}
			else {
				for(int idx2 = left.getKeysSize(); idx2 < ORDER; idx2++) {
					left.keys.add(right.getFirstKey());
					left.values.add(right.getFirstValue());
					right.keys.remove(0);
					right.values.remove(0);
				}
			}
			parent.keys.set(idx - 1, right.getFirstKey());

			return -1;
		} else {
			while (right.getKeysSize() > 0) {
				left.keys.add(right.getFirstKey());
				left.values.add(right.getFirstValue());
				right.keys.remove(0);
				right.values.remove(0);
			}
			if (right.next != null) {
				right.next.previous = left;
			}
			left.next = right.next;

			return idx - 1;
		}
	}

	public int fuseNode(IntermediateNode<K, V> leftInter, IntermediateNode<K, V> rightInter, IntermediateNode<K, V> parent) {
		parentFusionCnt++;
		int idx;
		for (idx = 0; idx < parent.getKeysSize(); idx++) {
			if (rightInter.getFirstKey().compareTo(parent.getKey(idx)) < 0) {
				break;
			}
		}

		if (leftInter.getKeysSize() + rightInter.getKeysSize() >= 2 * ORDER) {
			if (leftInter.getKeysSize() > rightInter.getKeysSize()) {
				for(int idx2 = ORDER; idx2 < leftInter.getKeysSize(); idx2++) {
					rightInter.keys.add(0, parent.getKey(idx - 1));
					rightInter.children.add(leftInter.getLastChild());
					parent.keys.set(idx - 1, leftInter.getLastKey());
					leftInter.keys.remove(leftInter.getKeysSize() - 1);
					leftInter.children.remove(leftInter.getChildrenSize() - 1);
				}
			} else {
				for(int idx2 = leftInter.getKeysSize(); idx2 < ORDER; idx2++) {
					leftInter.keys.add(parent.getKey(idx - 1));
					leftInter.children.add(rightInter.getFirstChild());
					parent.keys.set(idx - 1, rightInter.getFirstKey());
					rightInter.keys.remove(0);
					rightInter.children.remove(0);
				}
			}
			return -1;
		} else {
			leftInter.keys.add(parent.getKey(idx - 1));
			for(int idx2 = 0; idx2 < rightInter.getChildrenSize(); idx2++) {
				leftInter.keys.add(rightInter.getFirstKey());
				leftInter.children.add(rightInter.getFirstChild());
				rightInter.keys.remove(0);
				rightInter.children.remove(0);
			}
			leftInter.children.add(rightInter.getFirstChild());
			rightInter.children.remove(0);

			return idx - 1;
		}
	}
}