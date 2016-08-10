package org.tisong.proj3.connector.http;

import org.apache.catalina.util.StringManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tisong on 8/7/16.
 */
public class SocketInputStream extends InputStream{

    // -------------------------------------------------- Constants

    private static final byte CR = (byte) '\r';

    private static final byte LF = (byte) '\n';

    private static final byte SP = (byte) ' ';

    private static final byte HT = (byte) '\t';

    private static final byte COLON = (byte) ':';

    private static final int LC_OFFSET = 'A' - 'a';


    private byte buf[];

    private int count;

    private int pos;

    private InputStream is;

    private static StringManager sm = StringManager.getManager(Constants.Package);

    // ----------------------------------------------------

    public SocketInputStream(InputStream is, int bufferSize) {

        this.is = is;
        buf = new byte[bufferSize];
    }


    public void readHeader(HttpHeader header) throws IOException{


        if (header.nameEnd != 0) {
            header.recyle();
        }


        int chr = read();
        if (chr == CR || chr == LF) {
            if (chr == CR) {
                read();
            }
            header.nameEnd = 0;
            header.valueEnd = 0;
            return ;
        }


        pos--;

        int readCount = 0;
        while(true) {
            if (pos >= count) {
                if (read() == -1) {
                    throw new EOFException(sm.getString("requestStream.readline.error"));
                }
                pos = 0;
            }
            if (buf[pos] == COLON) {
                pos++;
                break;
            } else {
                header.name[readCount++] = (char)buf[pos++];
            }
        }
        header.nameEnd = readCount - 1;


        readCount = 0;
        /* 先跳过 : 后面的空白 SP | HT ; 而读取文字直到文字中的 SP | HT 不再忽略*/
        while(true) {
            while(true) {
                if (pos >= count) {
                    if (read() == -1) {
                        throw new EOFException(sm.getString("requestStream.readline.error"));
                    }
                    pos = 0;
                }
                if (buf[pos] == SP || buf[pos] == HT) {
                    pos++;
                } else {
                    break;
                }
            }

            while(true) {
                if (pos >= count) {
                    if (read() == -1) {
                        throw new EOFException(sm.getString("requestStream.readline.error"));
                    }
                }
                if(buf[pos] == CR) {
                    pos++;
                } else if (buf[pos] == LF) {
                    pos++;
                    break;
                } else {
                    header.value[readCount++] = (char)buf[pos++];
                }
            }

            int nextChr = read();
            if (nextChr != SP && nextChr != HT) {
                pos--;
                break;
            } else {
                header.value[readCount++] = ' ';
            }
        }

        header.valueEnd = readCount;
    }


    public void readRequestLine(HttpRequestLine requestLine) throws IOException {

        int chr = 0;

        do {
            try {
                chr = read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while ((chr == CR) || (chr == LF));
        if (chr == -1) {
            throw new EOFException();
        }
        pos--;

        boolean space = false;

        int maxRead = requestLine.method.length;
        int readCount = 0;

        while (true) {
            if (readCount >= maxRead) {

            }
            if (pos >= count) {
                if ( read() == -1 ) {

                }
                pos = 0;
            }
            if (buf[pos] == SP) {
                pos++;
                break;
            }
            requestLine.method[readCount++] = (char) buf[pos++];
        }


        readCount = 0;
        while (true) {
            if (pos >= count) {
                if (read() == -1) {
                    throw new IOException();
                }
                pos = 0;
            }
            if (buf[pos] == SP) {
                pos++;
                break;
            } else if (buf[pos] == CR || buf[pos] == LF ) {
                System.out.println("requestLine uri CR or LF");
                pos++;
                break;
            }
            requestLine.uri[readCount++] = (char) buf[pos++];
        }


        while(true) {
            if(readCount >= requestLine.protocol.length) {

            }
            if (buf[pos] == CR) {
                // skip
                pos++;
            } else if (buf[pos] == LF) {
                pos++;
                break;
            } else {
                requestLine.protocol[readCount++] = (char) buf[pos++];
            }
        }
    }

    /**
     * Read byte
     * @return byte
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        if (pos >= count) {
            fill();
            if (pos >= count)
                return -1;
        }

        return buf[pos++] & 0xff;
    }

    private void fill() throws IOException{
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0, buf.length);
        if (nRead > 0)
            count = nRead;
    }
}
