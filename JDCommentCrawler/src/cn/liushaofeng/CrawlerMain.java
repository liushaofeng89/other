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
public class CrawlerMain
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
            String sql = "INSERT INTO pd_db_comment_ (comment_id,comment_content,comment_product,comment_vote,comment_score,comment_user_reply,comment_user_nick,comment_user_location,comment_user_level,comment_user_client,comment_parent_id,comment_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
                    String sql1 = "INSERT INTO pd_db_comment_ (comment_id,comment_content,comment_product,comment_vote,comment_score,comment_user_reply,comment_user_nick,comment_user_location,comment_user_level,comment_user_client,comment_parent_id,comment_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
                    String sql2 = "INSERT INTO pd_db_comment_tag_ (comment_id,comment_tag_name,comment_tag_time) VALUES (?,?,?)";
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
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Cookie", "user-key=a11c5cd2-0669-42b1-988f-cfb8fecb2616; TrackID=1AuZlYnitAmsIxrKxZPTJDV30Avrjg_yIRuAMvN11X4fGELRw5Tx6IhDJU751WJKMp5IfexkZUzYHN2oyNbdv68-SpQ5n4fxUth4TzvEyE_M; pinId=iE7hiualroxNrsaubiSlVg; pin=76380418-704809; unick=jd_%E9%94%8B%E5%B0%91; _tp=zDFp6nmNXNXlabZhd7PL9A%3D%3D; _pst=76380418-704809; __jdv=122270672|direct|-|none|-|1501688947828; ipLocation=%u5317%u4EAC; areaId=22; ipLoc-djd=22-1930-49322-49422; _jrda=1; _jrdb=1501858618359; wlfstk_smdl=ieplfmjevgdwukl807l0zza00jgv4c9m; __jda=122270672.401462745.1486122634.1501784215.1501856851.22; __jdb=122270672.56.401462745|22.1501856851; __jdc=122270672; 3AB9D23F7A4B3C9B=KEVG62X335OP7OFUO6HPPBFPHLJHEYNVORFRUGJU5LWCUA4ECJTY4D7Y4NPGBANMOGDDT7MFQ5N7IS2WD3KKHYM2C4; cn=0; __jdu=401462745");
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
