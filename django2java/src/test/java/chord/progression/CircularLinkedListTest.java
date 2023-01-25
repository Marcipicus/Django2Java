package chord.progression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CircularLinkedListTest {
	
	private <T> CircularLinkedList<T> createList( T... elements) {
		CircularLinkedList<T> list = new CircularLinkedList<>();
		
		for(T element : elements) {
			list.addNode(element);
		}
		
		return list;
	}
	
	CircularLinkedList<Integer> referenceList,equalList,nonEqualList,equivalentList;
	
	@BeforeEach
	void init() {
		referenceList = createList(1,2,3);
		equalList = createList(1,2,3);
		nonEqualList = createList(2,3,5);
		equivalentList = createList(3,1,2);
	}
	
	@Test
	void testEquivalent() {
		assertTrue(referenceList.isEquivalent(equalList));
		assertFalse(referenceList.isEquivalent(nonEqualList));
		assertTrue(referenceList.isEquivalent(equivalentList));
	}
	
	@Test
	void testToString() {
		assertEquals("1->2->3",referenceList.toString());
	}
	
	@Test
	void testEquals() {
		assertEquals(referenceList,equalList);
		assertNotEquals(referenceList,nonEqualList);
		assertNotEquals(referenceList,equivalentList);
	}
	
	@Test
	void testHashCode() {
		assertEquals(referenceList.hashCode(),equalList.hashCode());
		assertNotEquals(referenceList.hashCode(),nonEqualList.hashCode());
		assertNotEquals(referenceList.hashCode(),equivalentList.hashCode());
	}

}
