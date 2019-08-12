package cn.liushaofeng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class PDDBUtils
{
    private static QueryRunner queryRunner = new QueryRunner();
    private static ArrayHandler arrayHandler = new ArrayHandler();
    private static Connection conn = null;

    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
        Class.forName(DBConf.JDBC_DRIVER);
        conn = DriverManager.getConnection(DBConf.DB_URL, DBConf.USER, DBConf.PASS);

        // 读取人工筛选的comment新老用户评论分类数据到数据库
//        insertCommentId2DB();

         translateId2Txt();

        if (null != conn)
        {
            conn.close();
        }
    }

    private static void translateId2Txt()
    {
        BufferedWriter bufferedWriter = null;
        try
        {
            List<Object[]> objs = queryRunner
                .query(
                    conn,
                    "SELECT pd_db_comment.comment_content FROM pd_db_comment LEFT OUTER JOIN pd_db_comment_customer ON pd_db_comment.comment_id=pd_db_comment_customer.comment_id WHERE pd_db_comment_customer.comment_customer_label=2",
                    new ArrayListHandler());
            bufferedWriter = new BufferedWriter(new FileWriter(new File("F:\\source\\oschina\\PDcompetition\\NewerOrOlder\\other_comment.txt")));
            for (Object[] objects : objs)
            {
                bufferedWriter.write(String.valueOf(objects[0]));
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.flush();

        } catch (SQLException e1)
        {
            e1.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (null != bufferedWriter)
            {
                try
                {
                    bufferedWriter.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertCommentId2DB() throws SQLException
    {
        BufferedReader bufferedReader = null;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(new File("F:\\source\\oschina\\PDcompetition\\NewerOrOlder\\new_comment_id1.txt")));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                doInsert(line.trim());
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (null != bufferedReader)
            {
                try
                {
                    bufferedReader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void doInsert(String commentId)
    {
        try
        {
            String sql2 = "INSERT INTO pd_db_comment_customer (comment_id,comment_customer_label) VALUES (?,?)";
            queryRunner.insert(conn, sql2, arrayHandler, commentId, 0);

        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }
}
