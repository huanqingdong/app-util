package app.util;

/**
 * @author faith.huan 2018-12-07 14:33
 */
public class PathUtil {

    /**
     * 获取class根路径
     * @return
     */
    public static String getClassPath(){
        return PathUtil.class.getResource("/").getFile().toString();
    }
}
