package mapsoft.com.costomtopbar.usb.partition.fs;

import android.support.annotation.Nullable;


import java.io.IOException;

import mapsoft.com.costomtopbar.usb.driver.BlockDeviceDriver;
import mapsoft.com.costomtopbar.usb.driver.ByteBlockDevice;
import mapsoft.com.costomtopbar.usb.fs.FileSystemFactory;
import mapsoft.com.costomtopbar.usb.partition.PartitionTable;
import mapsoft.com.costomtopbar.usb.partition.PartitionTableFactory;

/**
 * Created by magnusja on 30/07/17.
 */

public class FileSystemPartitionTableCreator implements PartitionTableFactory.PartitionTableCreator {
    @Nullable
    @Override
    public PartitionTable read(BlockDeviceDriver blockDevice) throws IOException {
        try {
            return new FileSystemPartitionTable(blockDevice,
                    FileSystemFactory.createFileSystem(null, new ByteBlockDevice(blockDevice)));
        } catch(FileSystemFactory.UnsupportedFileSystemException e) {
            return null;
        }
    }
}
