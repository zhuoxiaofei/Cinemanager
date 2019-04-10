package net.lzzy.cinemanager.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * @author lzzy_gxy
 * @date 2019/3/11
 * Description:
 */
@SuppressLint("Registered")
public class AppUtils extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static int px2dp(int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(int dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width   位图宽度(单位:dp)
     * @param height  位图高度(单位:dp)
     * @return
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height) {
        return createQRCodeBitmap(content, dp2px(width), dp2px(height), "UTF-8",
                "H", "2", Color.BLACK, Color.WHITE);
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content         字符串内容
     * @param width           位图宽度,要求>=0(单位:px)
     * @param height          位图高度,要求>=0(单位:px)
     * @param characterSet    字符集/字符转码格式 (支持格式:{@link CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param errorCorrection 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin          空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param colorBlack      黑色色块的自定义颜色值
     * @param colorWhite      白色色块的自定义颜色值
     * @return
     */
    private static Bitmap createQRCodeBitmap(String content, int width, int height, String characterSet,
                                             String errorCorrection, String margin,
                                             int colorBlack, int colorWhite) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            if (!TextUtils.isEmpty(characterSet)) {
                hints.put(EncodeHintType.CHARACTER_SET, characterSet);
            }
            if (!TextUtils.isEmpty(errorCorrection)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = colorBlack;
                    } else {
                        pixels[y * width + x] = colorWhite;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readQRCode(Bitmap qrCodeBitmap) {
        int width = qrCodeBitmap.getWidth();
        int height = qrCodeBitmap.getHeight();
        int[] pixels = new int[width * height];
        qrCodeBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap).getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            return null;
        }
    }
}
