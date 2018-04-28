package com.yumi.butler.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Base64;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2017/10/10.
 */
public class FileUtils {
    public static String ORIGINAL_PATH = "/Users/teddyzhou/Documents/Jobs/lavande/userHead/";
    public static void main(String[] args) {
//        try {
//            Map<String, List<Map<String, String>>> user = new HashMap<String, List<Map<String, String>>>();
//            readfile(user, ORIGINAL_PATH);
//        } catch (FileNotFoundException ex) {
//        } catch (IOException ex) {
//        }
        changeSilk("/Users/teddyzhou/Documents/Jobs/lavande/msg/171015075222673773569.silk");
    }

    public static void changeSilk(String filePath) {
        try {
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuilder lineTxt = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    lineTxt.append(line);
                }
                read.close();

                String olddata = lineTxt.toString();
                olddata = olddata.replace("data:audio/webm;base64,", "");

                try {

                    File webmFile = new File(file.getAbsolutePath().replace(".silk", ".webm"));
                    byte[] bt = Base64.decode(olddata.getBytes()) ;
                    FileOutputStream in = new FileOutputStream(webmFile);
                    try {
                        in.write(bt, 0, bt.length);
                        in.close();
                        // boolean success=true;
                        // System.out.println("写入文件成功");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }


    /**
     * 读取某个文件夹下的所有文件
     */
    public static boolean readfile(Map<String, List<Map<String, String>>> allUserMap, String filepath) throws FileNotFoundException, IOException {
        try {
            String innName = "";
            String fileName = "";
            String pinyinName = "";
            Map<String, String> userMap= null;
            File childFile = null;
            File newChildFile = null;

            File file = new File(filepath);
            if(file.isDirectory()){
                String[] childFilePathList = file.list();
                for (String childFilePath : childFilePathList){
                    childFile = new File(filepath + File.separator + childFilePath);
                    if (!childFile.isDirectory()) {
                        innName = childFile.getParentFile().getName();

                        if(!StringUtils.isEmpty(innName)){
                            if (!allUserMap.containsKey(innName)){
                                allUserMap.put(innName, new ArrayList<Map<String, String>>());
                            }

                            fileName = childFile.getName().substring(0,childFile.getName().lastIndexOf("."));
                            if(!StringUtils.isEmpty(fileName)){
                                pinyinName = PinyinUtil.makePinyin(fileName).split(",")[0];
                                userMap = new HashMap<String, String>();
                                userMap.put("account", pinyinName);
                                userMap.put("username", fileName);

                                allUserMap.get(innName).add(userMap);
                                //System.out.println(childFile.getAbsolutePath().replace(childFile.getName(), pinyinName+".png"));

                                //newChildFile = new File(childFile.getAbsolutePath().replace(childFile.getName(), pinyinName+".png"));
                                //childFile.renameTo(newChildFile);
                            }
                        }
                    }else{
                        readfile(allUserMap, filepath + File.separator + childFilePath);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return true;
    }
}
