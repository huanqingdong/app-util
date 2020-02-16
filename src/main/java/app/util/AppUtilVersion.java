package app.util;

/**
 * 获取版本
 *
 * @author faith.huan 2020-02-16 19:35
 */
public final class AppUtilVersion {
    private AppUtilVersion() {
    }

    public static String getVersion() {
        Package pkg = AppUtilVersion.class.getPackage();
        return pkg == null ? null : pkg.getImplementationVersion();
    }

}
