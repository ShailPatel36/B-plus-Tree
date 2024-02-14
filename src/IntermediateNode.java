import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class IntermediateNode<K extends Comparable<K>, V> extends Node<K, V> {

    protected ArrayList<Node<K, V>> children;

    public IntermediateNode(K key, Node<K, V> child_one, Node<K, V> child_two) {
        setLeaf(false);
        keys = new ArrayList<>();
        children = new ArrayList<>();
        keys.add(key);
        children.add(child_one);
        children.add(child_two);
    }

    public IntermediateNode(ArrayList<K> keys, ArrayList<Node<K, V>> children) {
        setLeaf(false);
        this.keys = keys;
        this.children = children;
    }

    public void insert(Pair<K, Node<K, V>> pair, int idx) {
        K key = pair.getKey();
        Node<K, V> child = pair.getValue();
        
        if(idx >= getKeysSize()) {
        	keys.add(key);
        	children.add(child);
        } else {
        	keys.add(idx, key);
        	children.add(idx + 1, child);
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < getKeysSize(); i++) {
            sb.append(getKey(i));

            if(i < getKeysSize() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public int getChildrenSize() {
    	return children.size();
    }
    
    public Node<K, V> getFirstChild() {
    	return getChild(0);
    }

    public Node<K, V> getLastChild() {
    	return getChild(getChildrenSize() - 1);
    }
    
    public Node<K, V> getChild(int index) {
    	if(getChildrenSize() == 0) {
    		return null;
    	}
    	return children.get(index);
    }

    public int findChildIndex(Node<K, V> child) {
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == child) {
                return i;
            }
        }
        return 0;
    }
}
