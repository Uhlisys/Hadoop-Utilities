package com.uhlisys.hadoop.utilities.hclean;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;

/**
 *
 */
public final class TestHelper {

    public static final String pathPattern = "(?<year>[0-9]+)/(?<month>[0-9]+)/(?<day>[0-9]+)/(?<hour>[0-9]+)/(?<minute>[0-9]+)/(?<second>[0-9]+)";
    public static final String relativePathString = "/2013/12/011/10/09/08/file";
    public static final String basePathString = "/path/to/test";
    public static final String absolutePathString = basePathString + relativePathString;
    public static final String filenameString = "file";
    public static final String testString = "fooBar";
    public static final String fileGroup = "testGroup";
    public static final String fileOwner = "testUser";
    public static final Path absolutePath = new Path(absolutePathString);
    public static final Path relativePath = new Path(relativePathString);
    public static final Path basePath = new Path(basePathString);
    public static final long filesize = 1073741824L; // 1 GB
    public static final long blocksize = 134217728L; // 128 MB
    public static final long mtime = 1386756548000L; // 2013-12-11 10:09:08
    public static final long atime = 1146711721000L; // 2006-05-04 03:02:01
    public static final String mtimestamp = "2013-12-11T10:09:08"; // 1386756548000
    public static final String atimestamp = "2006-05-04T03:02:01"; // 1146711721000
    public static final long weekInMs = 604800000;
    public static final long dayInMs = 86400000;
    public static final long hourInMs = 3600000;
    public static final long minuteInMs = 60000;
    public static final long secondInMs = 1000;

    public static final FileStatus fileStatus = new FileStatus(
            filesize, // length
            false, // isDirectory
            3, // Block Replication
            blocksize, // Block Size (134217728 bytes = 128 Megabytes)
            mtime, // Modification Time in MS
            atime, // Access Time
            new FsPermission(FsAction.READ_WRITE, FsAction.READ, FsAction.NONE, true), // FSPermissions
            fileOwner, // Owner
            fileGroup, // Group
            absolutePath);
    public static final FileStatus dirStatus = new FileStatus(
            0, // length
            true, // isDirectory
            1, // Block Replication
            blocksize, // Block Size (134217728 bytes = 128 Megabytes)
            mtime, // Modification Time in MS
            atime, // Access Time
            new FsPermission(FsAction.READ_WRITE, FsAction.READ, FsAction.NONE, true), // FSPermissions
            fileOwner, // Owner
            fileGroup, // Group
            absolutePath);    

    private TestHelper() {
    }
}
