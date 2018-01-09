package mapsoft.com.costomtopbar.usb.fs;

import android.support.annotation.Nullable;


import java.io.IOException;

import mapsoft.com.costomtopbar.usb.driver.BlockDeviceDriver;
import mapsoft.com.costomtopbar.usb.partition.PartitionTableEntry;

/**
 * Created by magnusja on 28/02/17.
 */

public interface FileSystemCreator {
    @Nullable
    FileSystem read(PartitionTableEntry entry, BlockDeviceDriver blockDevice) throws IOException;
}
