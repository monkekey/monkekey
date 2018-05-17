package com.hpd.butler.utils;

import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;

import java.io.IOException;

/**
 * Created by zy on 2017/10/23.
 */
public class VoiceUtils {
    public static void main(String[] args){
        String sourceSilk = "/Users/teddyzhou/Downloads/171023055026574913455.silk";
        String silkV3Url = "/Users/teddyzhou/Documents/silk-v3-decoder1/";

//        String sourceSilk = "E:\\teddyzhou\\silk-v3-decoder\\171023032206862855251.silk";
//        String silkV3Url = "E:\\teddyzhou\\silk-v3-decoder";

        System.out.println(silk2mp3(sourceSilk, silkV3Url));
    }

    /**
     * 解码为pcm格式
     * @param silk 源silk文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.silk
     * @param pcm 目标pcm文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.pcm
     * @param silkv3url silk-v3-decoder 绝对路径
     * @return
     */
    public static boolean getPcm(String silk, String pcm, String silkv3url){
        boolean flag = true;
        String cmd="cmd.exe /c " + silkv3url +"\\windows\\silk_v3_decoder.exe "+silk+" "+pcm+" -quiet";
        System.out.println("转码到pcm...");
        try
        {
            StringBuilder msg = Lang.execOutput(cmd, Encoding.CHARSET_GBK);
            //System.out.println(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 转码为MP3格式
     * @param pcm 源pcm文件,需要绝对路径!!  例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.pcm
     * @param mp3 目标mp3文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.mp3
     * @return
     */
    public static boolean getMp3(String pcm,String mp3, String silkv3url){
        boolean flag = true;
        System.out.println("转码到mp3...");
        try {
            StringBuilder sb = Lang.execOutput("cmd.exe /c " + silkv3url + "\\windows\\ffmpeg.exe -y -f s16le -ar 24000 -ac 1 -i "+pcm+" "+mp3+"", Encoding.CHARSET_GBK);
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * windows转码为MP3格式
     * @param sourceFile 源文件,需要绝对路径!!
     * @param silkv3url silk-v3-decoder 绝对路径
     * @return
     */
    public static boolean winSilk2mp3(String sourceFile, String silkv3url){
        if (!sourceFile.endsWith(".silk")){
            return false;
        }
        boolean flag = getPcm(sourceFile, sourceFile.replace(".silk", ".pcm"), silkv3url);
        if (flag){
            flag = getMp3(sourceFile.replace(".silk", ".pcm"), sourceFile.replace(".silk", ".mp3"), silkv3url);
        }
        return flag;
    }

    /**
     * linux转码为MP3格式
     * @param sourceFile 源文件,需要绝对路径!!
     * @param silkv3url silk-v3-decoder 绝对路径
     * @return
     */
    public static boolean linuxSilk2mp3(String sourceFile, String silkv3url){
        boolean flag = true;
        System.out.println("转码到mp3...");//StringBuilder sb = Lang.execOutput("sh /usr/local/silk-v3-decoder/converter.sh "+sourceFile+" mp3", Encoding.CHARSET_GBK);
        try {
            StringBuilder sb = Lang.execOutput("sh " + silkv3url + "/converter.sh "+sourceFile + " mp3", Encoding.CHARSET_GBK);
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static boolean silk2mp3(String sourceFile, String silkv3url){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return winSilk2mp3(sourceFile, silkv3url);
        }else {
            return linuxSilk2mp3(sourceFile, silkv3url);
        }
    }
}
