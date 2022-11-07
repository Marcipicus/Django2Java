package chord.relations.persist;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * This class exists to marshall(write object to file) and unmarshall(read object from file)
 * JAXB annotated classes.  
 * @author DAD
 *
 */
public class Persister {

	/**
	 * Write the object to the destination file.
	 * @param objectToPersist Object containing the data to be written to file.
	 * @param destinationFile file to which the data will be written.
	 * @throws JAXBException if there is an error writing the data to file.
	 */
	public static void marshall(Object objectToPersist, String destinationFile) throws JAXBException {

		if(objectToPersist == null) {
			throw new NullPointerException("objectToPersist may not be null");
		}
		if(destinationFile == null) {
			throw new NullPointerException("Destination file name may not be null.");
		}

		JAXBContext context = JAXBContext.newInstance(objectToPersist.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(objectToPersist,new File(destinationFile));
	}

	/**
	 * Load the data from the file at fileName into an object of the
	 * specified class.
	 * @param <T> Class that contains the data in the file
	 * @param fileName file from which to retrieve the file.
	 * @param persistableClass class type of the data stored in filename
	 * @return an object of the specified class that contains the data stored
	 * in filename
	 * @throws JAXBException if there is an error unmarshalling the data.
	 */
	public static <T> T unmarshall(String fileName, Class<T> persistableClass ) throws JAXBException {
		if(fileName == null) {
			throw new NullPointerException("File name may not be null.");
		}
		if(persistableClass == null) {
			throw new NullPointerException("persistable class may not be null");
		}
		
		JAXBContext context = JAXBContext.newInstance(persistableClass);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Object unmarshalled = unmarshaller.unmarshal(new File(fileName));
		return (T)unmarshalled;
	}
}
