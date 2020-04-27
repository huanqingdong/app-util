package app.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Es工具类
 *
 * @author faith.huan 2020-04-27 8:37
 */
@Slf4j
public class EsUtil {


    /**
     * ES查询时处理特殊符号
     *
     * @param path 路径
     * @return 处理后路径
     */
    public static String getPathForQuery(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        // 查询时需对/进行转义处理
        String pathForQuery = path.replace("/", "\\/");
        log.debug("EsUtil.getPathForQuery:{}->{}", path, pathForQuery);
        return pathForQuery;
    }
}
