package com.adaptris.flyway.test;

import com.adaptris.testing.DockerComposeFunctionalTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultFunctionalTest extends DockerComposeFunctionalTest {
    protected static String INTERLOK_SERVICE_NAME = "interlok-1";
    protected static String MARIADB_SERVICE_NAME = "mariadb-1";
    protected static int INTERLOK_PORT = 8080;
    protected static int MARIADB_PORT = 3306;
    protected static WaitStrategy defaultWaitStrategy = Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30));

    @Override
    protected ComposeContainer setupContainers() throws Exception {
        return new ComposeContainer(new File("docker-compose.yaml"))
                .withExposedService(INTERLOK_SERVICE_NAME, INTERLOK_PORT, defaultWaitStrategy)
                .withExposedService(MARIADB_SERVICE_NAME, MARIADB_PORT, defaultWaitStrategy);
    }



    @Test
    public void test() throws Exception {
        Thread.sleep(5000);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Exception e = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            InetSocketAddress address = getHostAddressForService(MARIADB_SERVICE_NAME, MARIADB_PORT);
            conn = DriverManager.getConnection(
                    String.format("jdbc:mariadb://%s:%d/accounts", address.getHostName(), address.getPort()), "root", "password");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM account";
            rs = stmt.executeQuery(sql);
            assertTrue(rs.next());
        } catch (Exception ex) {
            e = ex;
        } finally {
            if (rs != null) rs.close();
            try {
                if (stmt != null) conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
            }
        }
        assertNull(e);
    }
}
