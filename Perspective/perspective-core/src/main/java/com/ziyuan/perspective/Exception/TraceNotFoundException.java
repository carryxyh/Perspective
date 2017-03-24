
package com.ziyuan.perspective.Exception;

/**
 * TraceNotFoundException trace拿不到的异常
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public class TraceNotFoundException extends Exception {

    public TraceNotFoundException() {
        super("Trace not found !");
    }
}
