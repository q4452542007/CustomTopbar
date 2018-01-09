package mapsoft.com.costomtopbar.map2312;

/**
 * @author djl
 * @function
 */

public class PathPlateMsg  {

    //// 路牌编号
    int plateNum;

    // 路牌属性
    int plateProps;

    // 线路号
    String pathNum;

    //显示风格
    int displayStyle;

    //几字路牌
    int words;

    //显示内容
    byte[]  content;


    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(int plateNum) {
        this.plateNum = plateNum;
    }

    public int getPlateProps() {
        return plateProps;
    }

    public void setPlateProps(int plateProps) {
        this.plateProps = plateProps;
    }

    public String getPathNum() {
        return pathNum;
    }

    public void setPathNum(int pathNum) {
        this.pathNum = "[" + pathNum + "]";
    }

    public int getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(int displayStyle) {
        this.displayStyle = displayStyle;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
