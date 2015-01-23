package nss;

/**
 * Created by president on 1/23/15.
 */
public class Libs {

    public boolean fileIsImage(String filePath) {
        String fileFormat = getFileType(filePath).toLowerCase();
        if (
                fileFormat.equals("jpg") || fileFormat.equals("jpeg") || (fileFormat.equals("nef") ||
                        fileFormat.equals("cr2") || fileFormat.equals("psd")  ||fileFormat.equals("tif")   ||
                        fileFormat.equals("png") || fileFormat.equals("gif")  || fileFormat.equals("orf")  ||
                        fileFormat.equals("arw") || fileFormat.equals("rw2")))
            return true;
        else
            return false;
    }

    public String getFileType(String fullPathToImage) {
        String[] arrPath = fullPathToImage.split("\\.");
        String type = arrPath[(arrPath.length-1)];
        return type;
    }
}
