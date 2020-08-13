package app.util;

import junit.framework.TestCase;

/**
 * @author faith.huan 2020-07-25 13:12
 */
public class FileSizeUtilTest extends TestCase {

    public void testFormatFileSize() {

        String fileSize = FileSizeUtil.formatFileSize(112121);
        System.out.println("fileSize = " + fileSize);
    }
}
