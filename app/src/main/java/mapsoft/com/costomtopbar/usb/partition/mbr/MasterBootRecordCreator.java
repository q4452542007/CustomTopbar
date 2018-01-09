package mapsoft.com.costomtopbar.usb.partition.mbr;

import android.support.annotation.Nullable;



import java.io.IOException;
import java.nio.ByteBuffer;

import mapsoft.com.costomtopbar.usb.driver.BlockDeviceDriver;
import mapsoft.com.costomtopbar.usb.partition.PartitionTable;
import mapsoft.com.costomtopbar.usb.partition.PartitionTableFactory;

/**
 * Created by magnusja on 30/07/17.
 */

public class MasterBootRecordCreator implements PartitionTableFactory.PartitionTableCreator {
    @Nullable
    @Override
    public PartitionTable read(BlockDeviceDriver blockDevice) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        blockDevice.read(0, buffer);
        return MasterBootRecord.read(buffer);
    }
}
