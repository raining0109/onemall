package cn.sunzhichao.mall.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class AliyunOSSUtil {

    public static final Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    private static String endpoint = PropertiesUtil.getProperty("aliyunoss.endpoint");
    private static String accessKeyId = PropertiesUtil.getProperty("aliyunoss.accessKeyId");
    private static String accessKeySecret = PropertiesUtil.getProperty("aliyunoss.accessKeySecret");
    private static String bucketName = PropertiesUtil.getProperty("aliyunoss.bucketName");

    public static boolean uploadFile(String fileName, File file) throws IOException {

        logger.info("开始连接阿里云OSS服务器");
        boolean result = upload(fileName, file);
        logger.info("结束成功");
        return result;
    }

    private static boolean upload(String fileName, File file) throws IOException {

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {

            if (ossClient.doesBucketExist(bucketName)) {
                logger.info("您已经创建Bucket：" + bucketName + "。");
            } else {
                logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
                ossClient.createBucket(bucketName);
            }

            //完成上传
            ossClient.putObject(bucketName, "img/" + fileName, file);

        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        return true;
    }
}