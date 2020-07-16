package app.util;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import java.util.Properties;

/**
 * jdbcTemplate工具类,用户创建
 *
 * @author faith.huan 2019-10-30 14:06
 */
@Slf4j
public class JdbcTemplateBuilder {

    private static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String IMPALA_DRIVER = "com.cloudera.impala.jdbc41.Driver";

    private static final String ORACLE_URL = "jdbc:oracle:thin:@%s";
    private static final String IMPALA_URL = "jdbc:impala://%s";
    private static final String IMPALA_URL_KBS = "jdbc:impala://%s:%s/default;AuthMech=1;KrbRealm=WEICHAI.COM;KrbHostFQDN=%s;KrbServiceName=impala";


    /**
     * 构建oracle jdbcTemplate
     *
     * @param ipPortSid 192.168.1.1:1521:orcl
     * @param username  scott
     * @param password  tiger
     * @return template
     */
    public static JdbcTemplate buildOracle(String ipPortSid, String username, String password) {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(ORACLE_DRIVER);
        dataSource.setUrl(String.format(ORACLE_URL, ipPortSid));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(false);

        return new JdbcTemplate(dataSource);
    }


    /**
     * 构建ImpalaJdbcTemplate
     *
     * @param host 主机名
     * @return jdbcTemplate
     */
    public static JdbcTemplate buildImpala(String host) {
        return buildImpala(host, 21050);
    }


    /**
     * 构建ImpalaJdbcTemplate
     *
     * @param host 主机名
     * @param port 端口
     * @return jdbcTemplate
     */
    public static JdbcTemplate buildImpala(String host, int port) {
        String hostPort = host + ":" + port;
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(IMPALA_DRIVER);
        dataSource.setUrl(String.format(IMPALA_URL, hostPort));
        dataSource.setTestWhileIdle(false);
        return new JdbcTemplate(dataSource);
    }


    /**
     * 构建ImpalaJdbcTemplate
     *
     * @param host 主机名
     * @return jdbcTemplate
     */
    public static JdbcTemplate buildImpalaKbs(String host, String user) throws IOException {
        return buildImpalaKbs(host, 21050, user);
    }

    /**
     * 构建ImpalaJdbcTemplate
     *
     * @param host 主机名
     * @param port 端口
     * @return jdbcTemplate
     */
    public static JdbcTemplate buildImpalaKbs(String host, int port, String user) throws IOException {
        DruidDataSource dataSource = new DruidDataSource();
        String confPath = JdbcTemplateBuilder.class.getResource("/").getFile();
        log.info("启用kerberos,配置文件路径:{}", confPath);
        String krb5Conf = confPath + "krb5.conf";
        System.setProperty("java.security.krb5.conf", krb5Conf);

        Configuration configuration = new Configuration();
        configuration.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(configuration);
        // app_shy.keytab
        String keytabPath = confPath + user + ".keytab";

        FilterAdapter filterAdapter = new FilterAdapter() {

            @SneakyThrows
            @Override
            public ConnectionProxy connection_connect(FilterChain chain, Properties info) throws SQLException {
                UserGroupInformation.loginUserFromKeytab(user + "@WEICHAI.COM", keytabPath);
                UserGroupInformation loginUser = UserGroupInformation.getLoginUser();
                log.info("当前kerberos用户:" + loginUser);
                return loginUser.doAs(new PrivilegedAction<ConnectionProxy>() {
                    @SneakyThrows
                    public ConnectionProxy run() {
                        return chain.connection_connect(info);
                    }
                });
            }
        };


        dataSource.getProxyFilters().add(filterAdapter);
        dataSource.setDriverClassName(IMPALA_DRIVER);
        dataSource.setUrl(String.format(IMPALA_URL_KBS, host, port, host));
        dataSource.setTestWhileIdle(false);
        return new JdbcTemplate(dataSource);
    }

    /**
     * 关闭数据源
     *
     * @param jdbcTemplate t
     */
    public static void close(JdbcTemplate jdbcTemplate) {
        if (jdbcTemplate != null) {
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource) dataSource).close();
            }

        }
    }

}
