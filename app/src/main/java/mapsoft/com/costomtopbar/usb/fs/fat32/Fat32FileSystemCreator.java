package mapsoft.com.costomtopbar.usb.fs.fat32;



import java.io.IOException;

import mapsoft.com.costomtopbar.usb.driver.BlockDeviceDriver;
import mapsoft.com.costomtopbar.usb.fs.FileSystem;
import mapsoft.com.costomtopbar.usb.fs.FileSystemCreator;
import mapsoft.com.costomtopbar.usb.partition.PartitionTableEntry;

/**
 * Created by magnusja on 28/02/17.
 */

public class Fat32FileSystemCreator implements FileSystemCreator {

    @Override
    public FileSystem read(PartitionTableEntry entry, BlockDeviceDriver blockDevice) throws IOException {
        return Fat32FileSystem.read(blockDevice);
    }
}
