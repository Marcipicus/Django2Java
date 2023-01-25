package chord.progression;

/**
 * Node used in the circular linked list.
 * @author DAD
 *
 * @param <T> type of data stored in the circular linked list.
 */
class Node<T>{
	private T value;
	private Node<T> previousNode,nextNode;

	public Node(T value){
		setValue(value);
	}

	/**
	 * Get the value stored in the node.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Set the value of the node.
	 * @param value new value;
	 * @return the previous value stored in the node.
	 */
	public T setValue(T value) {
		if(value == null) {
			throw new NullPointerException("Nodes may not contain null values.");
		}
		T previousValue = this.value;
		this.value = value;

		return previousValue;
	}

	/**
	 * Get the previous node in the circular linked list.
	 * @return previous node.
	 */
	public Node<T> getPreviousNode() {
		return previousNode;
	}

	/**
	 * Set the previous node in the circular linked list 
	 * to the given node.
	 * @param previousNode new previous node.
	 */
	public void setPreviousNode(Node<T> previousNode) {
		this.previousNode = previousNode;
	}

	/**
	 * Get the next node in the circular linked list
	 * @return the next node in the ciurcular linked list.
	 */
	public Node<T> getNextNode() {
		return nextNode;
	}

	/**
	 * Set the next node in the circular linked list.
	 * @param nextNode the new next node
	 */
	public void setNextNode(Node<T> nextNode) {
		this.nextNode = nextNode;
	}

}

