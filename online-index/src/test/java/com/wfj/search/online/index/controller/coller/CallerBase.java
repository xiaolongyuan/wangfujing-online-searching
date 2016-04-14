package com.wfj.search.online.index.controller.coller;

import com.wfj.platform.util.signature.keytool.KeyUtils;
import com.wfj.platform.util.signature.keytool.RsaKeyGenerator;
import org.junit.After;
import org.junit.Before;

import java.security.KeyPair;
import java.sql.*;

/**
 * <p>create at 15-12-17</p>
 *
 * @author liufl
 * @since 1.0.0
 */
public abstract class CallerBase implements AutoCloseable {
    protected abstract Connection getConnection() throws ClassNotFoundException, SQLException;
    protected abstract String getCaller();
    protected KeyPair keyPair;

    @Before
    public void setUp() throws Exception {
        this.keyPair = RsaKeyGenerator.keyGen(2048);
        Connection connection = this.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement
                .executeQuery("SELECT sid FROM operator WHERE operator = '" + this.getCaller() + "'");
        resultSet.beforeFirst();
        long sid = 0;
        if (resultSet.next()) {
            sid = resultSet.getLong("sid");
        }
        String sql;
        if (sid == 0) {
            sql = "INSERT INTO `operator` (`rsa_key`, `operator`) VALUES (?, ?)";
        } else {
            sql = "UPDATE operator SET rsa_key = ? WHERE operator = ?";
        }
        PreparedStatement preparedStatement = connection
                .prepareStatement(sql);
        preparedStatement.setString(1, KeyUtils.toBase64String(keyPair.getPublic()));
        preparedStatement.setString(2, this.getCaller());
        preparedStatement.execute();
        connection.commit();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Override
    public void close() throws Exception {
        this.getConnection().close();
    }
}
