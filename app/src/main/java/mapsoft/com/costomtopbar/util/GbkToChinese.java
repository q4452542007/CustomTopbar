package mapsoft.com.costomtopbar.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import mapsoft.com.costomtopbar.view.IconCut;

/**
 * @author djl
 * @function
 */

public class GbkToChinese {

    private static final String GBK = "GBK";

    private static GbkToChinese sGbkToChinese;

    public GbkToChinese getInstance() {
        if (sGbkToChinese == null) {
            sGbkToChinese = new GbkToChinese();
        }
        return sGbkToChinese;
    }
    public String toString(String str) throws UnsupportedEncodingException {
        String encoded = "str";
        byte a0 = (byte) Integer.parseInt(encoded.substring(0, 2), 16);
        byte a1 = (byte) Integer.parseInt(encoded.substring(2), 16);
        byte[] gbk = new byte[] {a0, a1};
        String chinese = null;
        chinese = new String(gbk, GBK);
        return chinese;
    }
}
