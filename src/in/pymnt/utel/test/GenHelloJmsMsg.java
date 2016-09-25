/*
* Copyright (c) 2016, BON BIZ IT Services Pvt LTD.
*
* The Universal Permissive License (UPL), Version 1.0
* 
* Subject to the condition set forth below, permission is hereby granted to any person obtaining a copy of this software, associated documentation and/or data (collectively the "Software"), free of charge and under any and all copyright rights in the Software, and any and all patent rights owned or freely licensable by each licensor hereunder covering either (i) the unmodified Software as contributed to or provided by such licensor, or (ii) the Larger Works (as defined below), to deal in both

* (a) the Software, and

* (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if one is included with the Software (each a “Larger Work” to which the Software is contributed by such licensors),
* 
* without restriction, including without limitation the rights to copy, create derivative works of, display, perform, and distribute the Software and make, use, sell, offer for sale, import, export, have made, and have sold the Software and the Larger Work(s), and to sublicense the foregoing rights on either these or other terms.
* 
* This license is subject to the following condition:
* 
* The above copyright notice and either this complete permission notice or at a minimum a reference to the UPL must be included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
* 
* Author: Ashish Banerjee, ashish@bonbiz.in
*/

package in.pymnt.utel.test;

import in.pymnt.event.EventCentral;
import in.pymnt.event.ExitEvent;
import in.pymnt.utel.jms.TxtMsgSender;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ashish
 */
public class GenHelloJmsMsg implements Runnable {

    private long iterations = 10000;
    private long snooze = 10000; // 10 seconds
    private long pause = 1000; // 1 seconds
    private boolean shutdown = true;
    TxtMsgSender sender = null;
    String prefix = "Time Stamp [";
    String postfix = "]";

    private String loggerName = "JIO";

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
    
    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public long getSnooze() {
        return snooze;
    }

    public void setSnooze(long snooze) {
        this.snooze = snooze;
    }

    public long getPause() {
        return pause;
    }

    public void setPause(long pause) {
        this.pause = pause;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public TxtMsgSender getSender() {
        return sender;
    }

    public void setSender(TxtMsgSender sender) {
        this.sender = sender;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(snooze);
        } catch (InterruptedException e) {
        }
        DateFormat dtFmt = DateFormat.getTimeInstance();

        for (long i = 0; i < iterations; i++) {
            try {
                String msg = prefix + dtFmt.format(new Date()) + postfix;
                sender.send(msg);
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException e) {
                }
            } catch (IOException ex) {
                Logger.getLogger(loggerName).log(Level.SEVERE, null, ex);
            }
        }
        if (shutdown) {
            try {
                Thread.sleep(snooze);
            } catch (InterruptedException e) {
            }
            EventCentral.publish(new ExitEvent());
        }
    }

}
