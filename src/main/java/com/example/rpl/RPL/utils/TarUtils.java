package com.example.rpl.RPL.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.web.multipart.MultipartFile;

public class TarUtils {

    private TarUtils() {

    }


    // Based on https://stackoverflow.com/questions/27866976/how-do-i-create-a-tar-or-tar-gz-archive-entirely-from-objects-in-memory-no-file
    public static byte[] compressToTarGz(MultipartFile[] files) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        TarArchiveOutputStream gzOut = null;
        try {

    /*In this case I chose to show how to lay down a gzipped archive.
      You could just as easily remove the GZIPOutputStream to lay down a plain tar.
      You also should be able to replace the FileOutputStream with
      a ByteArrayOutputStream to lay nothing down on disk if you wanted
      to do something else with your newly created archive
    */
            System.err.println("Entered try");
            gzOut = new TarArchiveOutputStream(
                new GZIPOutputStream(
//                    new BufferedOutputStream(
                    bos
//                    )
                ));

            System.err.println("Created TarArchiveOutputStream");

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();

                System.err.println("Processing file " + fileName);

                //Name your entry with the complete path relative to the base directory
                //of your archive. Any directories that don't exist (e.g. "testDir") will
                //be created for you
                TarArchiveEntry textFile = new TarArchiveEntry(
                    fileName != null ? fileName : String.format("FILE_WITHOUT_NAME_%d", i));

                System.err.println("Created TarArchiveEntry");

                //Make sure to set the size of the entry. If you don't you will not be able
                //to write to it
                textFile.setSize(bytes.length);
                System.err.println("set size");

                //When you put an ArchiveEntry into the archive output stream,
                //it sets it as the current entry
                gzOut.putArchiveEntry(textFile);
                System.err.println("Put archive entry");

                //The write command allows you to write bytes to the current entry
                //on the output stream, which was set by the above command.
                //It will not allow you to write any more than the size
                //that you specified when you created the archive entry above
                gzOut.write(bytes);

                System.err.println("bytes written");

                gzOut.closeArchiveEntry();

            }

            //You must close the current entry when you are done with it.
            //If you are appending multiple archive entries, you only need
            //to close the last one. The putArchiveEntry automatically closes
            //the previous "current entry" if there was one
//            gzOut.closeArchiveEntry();

            System.err.println("tar CLOSED");

            gzOut.close();

            return bos.toByteArray();

        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
        } finally {
            if (gzOut != null) {
                try {
                    gzOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new byte[0];
    }
}
