/*
 * (C) Copyright 2014 mjahnen <jahnen@in.tum.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package mapsoft.com.costomtopbar.usb.driver.scsi.commands;



import java.nio.ByteBuffer;

/**
 * This class is used to issue a SCSI request sense when a command has failed.
 * 
 * @author mjahnen
 * @see com.github.mjdev.libaums.driver.scsi.commands.CommandStatusWrapper
 *      #getbCswStatus()
 */
public class ScsiRequestSense extends CommandBlockWrapper {
    private static final byte OPCODE = 0x3;
    private static final byte LENGTH = 0x6;

    private byte allocationLength;

    public ScsiRequestSense(byte allocationLength) {
        super(0, Direction.NONE, (byte) 0, LENGTH);
        this.allocationLength = allocationLength;
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.put(OPCODE);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.put((byte) 0);
        buffer.put(allocationLength);
    }

}
