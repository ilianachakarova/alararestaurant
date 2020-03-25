package alararestaurant.util;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface XmlParser {
    public <O> O parseXml(Class<O> ObjectClass, String filePath) throws JAXBException, FileNotFoundException;
}
