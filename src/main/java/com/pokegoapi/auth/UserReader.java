package com.pokegoapi.auth;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class UserReader {
    public static String GOOGLE_USERS = "src/resources/users/google.txt";
    public static String PTC_USERS = "src/resources/users/ptc.txt";

    protected int inputLineNumber = 0;
    protected int skippedLineNumber = 0;
    protected boolean isEof = false;
    protected BufferedReader basicAuthReader;

    public UserReader(BufferedReader basicAuthReader) {
        this.basicAuthReader = basicAuthReader;
    }

    public static int countLines(final String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public int getInputLineNumber() {
        return inputLineNumber;
    }

    public int getSkippedLineNumber() {
        return skippedLineNumber;
    }

    public boolean isEof() {
        return isEof;
    }

    public List<String[]> getMultiple(final int maxCount) throws IOException {
        String line;
        List<String[]> users = new LinkedList<>();

        while ((line = basicAuthReader.readLine()) != null) {
            inputLineNumber++;

            if (line.startsWith("#")) {
                skippedLineNumber++;
                continue; // skip lines with #
            }

            String[] cols = line.split(";");

            users.add(cols);

            if (users.size() >= maxCount) {
                break;
            }
        }

        if (line == null) {
            isEof = true;
        }

        return users;
    }

    public String[] getSingle() throws IOException {
        List<String[]> users = getMultiple(1);
        if (users.size() == 0)
            return null;
        return users.get(0);
    }

    public void close() {
        try {
            basicAuthReader.close();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }


    public static UserReader getUserReader(final String userFile) throws FileNotFoundException {
        if (userFile == null || userFile.equals("")) {
            throw new FileNotFoundException(String.format("File %s is empty. Working directory is %s", userFile, System.getProperty("user.dir")));
        }

        File file = new File(userFile);

        if (!file.exists()) {
            throw new FileNotFoundException("Cannot find file " + userFile);
        }

        BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));

        return new UserReader(bufferedReader);
    }
}