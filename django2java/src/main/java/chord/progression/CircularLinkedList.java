package chord.progression;

public class CircularLinkedList<T> {
	private Node<T> head = null;
	private Node<T> tail = null;

	public CircularLinkedList() {
		super();
	}

	/**
	 * Add the value to the list. value must be non-null.
	 * @param value value to add to the list.
	 */
	public void addNode(T value) {
		Node<T> newNode = new Node<>(value);

		if (head == null) {
			head = newNode;
		} else {
			tail.setNextNode(newNode);
		}

		tail = newNode;
		tail.setNextNode(head);
	}

	/**
	 * Test to see if the searchValue is contained in the list.
	 * @param searchValue value to search for.(Must be non-null)
	 * @return true if the value is in the list, false otherwise.
	 */
	public boolean containsNode(T searchValue) {
		if(searchValue == null) {
			throw new NullPointerException("searchValue may not be null.");
		}
		Node<T> currentNode = head;

		if (head == null) {
			return false;
		} else {
			do {
				if (currentNode.getValue().equals(searchValue)) {
					return true;
				}
				currentNode = currentNode.getNextNode();
			} while (currentNode != head);
			return false;
		}
	}

	/**
	 * Delete the first occurrence of the valueToDelete.
	 * @param valueToDelete value that will be deleted. (msut be non-null)
	 */
	public void deleteNode(T valueToDelete) {
		if(valueToDelete == null) {
			throw new NullPointerException("valueToDelete may not be null.");
		}

		Node<T> currentNode = head;
		if (head == null) { // the list is empty
			return;
		}
		do {
			Node<T> nextNode = currentNode.getNextNode();
			if (nextNode.getValue().equals(valueToDelete)) {
				if (tail == head) { // the list has only one single element
					head = null;
					tail = null;
				} else {
					currentNode.setNextNode(nextNode.getNextNode());
					if (head == nextNode) { //we're deleting the head
						head = head.getNextNode();
					}
					if (tail == nextNode) { //we're deleting the tail
						tail = currentNode;
					}
				}
				break;
			}
			currentNode = nextNode;
		} while (currentNode != head);
	}

	public int size() {
		int size = 0;
		Node<T> currentNode = head;

		if (head != null) {
			do {
				size++;
				currentNode = currentNode.getNextNode();
			} while (currentNode != head);
		}

		return size;
	}

	/**
	 * Compare the two lists and test to see if they represent
	 * the same cycle in any order of their analysis. 
	 * @param other list to be compared to
	 * @return true if the two lists represent the same cycle
	 * in any order of their comaprison
	 */
	public boolean isEquivalent(CircularLinkedList<T> other) {
		//ensure other is non-null and of equal size
		if( other == null || (this.size() != other.size()) ) {
			return false;
		}

		int sizeOfList = this.size();

		if(sizeOfList == 0) {
			return true;
		}

		//Node used as the start for comparison
		Node<T> thisStartNode;
		thisStartNode = this.head;

		int numSuccessfulComparisons;

		//Iterate over both lists using a different starting point for
		//the list on which the method was called.
		do {
			numSuccessfulComparisons = 0;
			Node<T> thisCurrentNode,otherCurrentNode;
			thisCurrentNode = thisStartNode;
			otherCurrentNode = other.head;

			//compare each node in the lists incrementing
			//the count of successful comparisons each time
			do {
				//compare the current nodes and break if they
				//do not have the same value
				if(!thisCurrentNode.getValue().equals(otherCurrentNode.getValue())) {
					break;
				}
				numSuccessfulComparisons++;
				thisCurrentNode = thisCurrentNode.getNextNode();
				otherCurrentNode = otherCurrentNode.getNextNode();
			}while(otherCurrentNode != other.head);

			//if we have finished the loop and we have the same number
			//of successful comparisons as the size of the list then
			//the two lists are equivalent
			if(numSuccessfulComparisons == sizeOfList) {
				return true;
			}
			//move on to the next node to use as the start node
			thisStartNode = thisStartNode.getNextNode();
		} while (thisStartNode != head);

		return false;
	}

	@Override
	public String toString() {
		if(head == null) {
			return "EMPTY LIST";
		}

		String text = "";

		Node<T> currentNode = head;
		do {
			text += currentNode.getValue().toString();
			currentNode = currentNode.getNextNode();

			if(currentNode == head) {
				break;
			}
			text += "->";
		} while (true);

		return text;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof CircularLinkedList))
			return false;

		CircularLinkedList<T> other = (CircularLinkedList<T>) o;

		Node<T> thisCurrentNode,otherCurrentNode;
		thisCurrentNode = this.head;
		otherCurrentNode = other.head;
		
		do {
			if( !thisCurrentNode.getValue().equals(otherCurrentNode.getValue())) {
				return false;
			}
			thisCurrentNode = thisCurrentNode.getNextNode();
			otherCurrentNode = otherCurrentNode.getNextNode();

		} while (thisCurrentNode != head);

		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 53;
		
		int result = prime;

		if(this.head == null) {
			return result;
		}

		Node<T> currentNode = this.head;
		do {
			result = prime*result + currentNode.getValue().hashCode();
			currentNode = currentNode.getNextNode();
		}while(currentNode != head);
		
		return result;
	}
}
