package cn.liushaofeng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * 爬虫入口
 * @author liushaofeng
 * @date 2017年8月3日 下午9:09:59
 * @version 1.0.2
 */
public class Main
{
    private static QueryRunner queryRunner = new QueryRunner();
    private static ArrayHandler arrayHandler = new ArrayHandler();
    private static String logFilePath = "F:\\data\\jd.txt";

    // 第一个p后面的参数是商品的id, s为评论的类型（追加与不追加），t暂时还不知道，p为页数
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("参数不合法！格式为：productId totalPageSize");
            return;
        }

        String prodcutId = args[0];
        int totalPage = Integer.parseInt(args[1]);
        BufferedWriter bufferedWriter = createWriter();

        for (int page = 0; page < totalPage; page++)
        {
            JsonArray doGet = doGet(initURL(prodcutId, page), bufferedWriter);
            if (null == doGet)
            {
                continue;
            }

            for (JsonElement jsonElement : doGet)
            {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                insertData(asJsonObject);
            }
        }

        closeWriter(bufferedWriter);
        System.out.println("程序执行完毕！");
    }

    private static void closeWriter(BufferedWriter bufferedWriter)
    {
        if (null != bufferedWriter)
        {
            try
            {
                bufferedWriter.close();
            } catch (IOException e)
            {
                e.printStackTrace();
                bufferedWriter = null;
            }
        }

    }

    private static BufferedWriter createWriter()
    {
        try
        {
            return new BufferedWriter(new FileWriter(new File(logFilePath)));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String initURL(String prodcutId, int page)
    {
        StringBuilder sb = new StringBuilder("http://club.jd.com/productpage/p-");
        sb.append(prodcutId);
        sb.append("-s-0-t-0-p-");
        sb.append(page);
        sb.append(".html");
        return sb.toString();
    }

    private static void insertData(JsonObject jsonObj)
    {
        Connection conn = null;

        try
        {
            Class.forName(DBConf.JDBC_DRIVER);
            conn = DriverManager.getConnection(DBConf.DB_URL, DBConf.USER, DBConf.PASS);

            // 插入评论
            String sql = "INSERT INTO pd_db_comment (comment_id,comment_content,comment_product,comment_vote,comment_score,comment_user_reply,comment_user_nick,comment_user_location,comment_user_level,comment_user_client,comment_parent_id,comment_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            queryRunner.insert(conn, sql, arrayHandler, jsonObj.get("id").toString(), jsonObj.get("content").getAsString(), jsonObj.get("referenceName")
                .getAsString(), jsonObj.get("usefulVoteCount").getAsInt(), jsonObj.get("score").getAsInt(), jsonObj.get("replyCount").getAsInt(),
                jsonObj.get("nickname").getAsString(), jsonObj.get("userProvince").getAsString(), jsonObj.get("userLevelName").getAsString(),
                jsonObj.get("userClientShow").getAsString(), "", jsonObj.get("creationTime").getAsString());

            // 获取评论回复
            JsonArray asJsonArray = jsonObj.getAsJsonArray("replies");
            if (asJsonArray != null)
            {
                for (JsonElement jsonElement : asJsonArray)
                {
                    JsonObject obj = jsonElement.getAsJsonObject();
                    String sql1 = "INSERT INTO pd_db_comment (comment_id,comment_content,comment_product,comment_vote,comment_score,comment_user_reply,comment_user_nick,comment_user_location,comment_user_level,comment_user_client,comment_parent_id,comment_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    queryRunner.insert(conn, sql1, arrayHandler, obj.get("id").toString(), obj.get("content").getAsString(), "", 0, 0, 0, obj.get("nickname")
                        .getAsString(), obj.get("userProvince").getAsString(), obj.get("userLevelName").getAsString(), obj.get("userClientShow").getAsString(),
                        obj.get("commentId").getAsString(), obj.get("creationTime").getAsString());
                }
            }

            // 更新标签
            JsonArray asJsonArray2 = jsonObj.getAsJsonArray("commentTags");
            if (asJsonArray2 != null)
            {
                for (JsonElement jsonElement : asJsonArray2)
                {
                    JsonObject obj = jsonElement.getAsJsonObject();
                    String sql2 = "INSERT INTO pd_db_comment_tag (comment_id,comment_tag_name,comment_tag_time) VALUES (?,?,?)";
                    queryRunner.insert(conn, sql2, arrayHandler, obj.get("commentId").toString(), obj.get("name").getAsString(), obj.get("created")
                        .getAsString());
                }
            }

        } catch (SQLException sqle)
        {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } finally
        {
            if (null != conn)
            {
                try
                {
                    conn.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                    conn = null;
                }
            }
        }
    }

    private static JsonArray doGet(String url, BufferedWriter bufferedWriter)
    {
        CloseableHttpClient createDefault = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try
        {
            CloseableHttpResponse response = createDefault.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                writeToFile(bufferedWriter, data);
                JsonElement parse = new JsonParser().parse(data);
                if (parse.isJsonObject())
                {
                    return parse.getAsJsonObject().getAsJsonArray("comments");
                }
            }
        } catch (JsonSyntaxException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeToFile(BufferedWriter bufferedWriter, String data)
    {
        try
        {
            bufferedWriter.write(data);
            bufferedWriter.write("\r\n");
            bufferedWriter.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
