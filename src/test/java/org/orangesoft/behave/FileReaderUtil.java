package org.orangesoft.behave;

import java.io.File;
import java.net.URISyntaxException;

public class FileReaderUtil {

    public static String getAbsolutePathFromResource(String resource) {
        try {
            return new File(ReportInformationTest.class.getClassLoader().getResource(resource).toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not read resource: " + resource, e);
        }
    }

}
