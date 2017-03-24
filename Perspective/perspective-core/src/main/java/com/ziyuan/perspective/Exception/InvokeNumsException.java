
package com.ziyuan.perspective.Exception;

/**
 * InvokeNumsException invoke数量异常,一个Trace中的invoke数量超过了最大值
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public class InvokeNumsException extends Exception {

    public InvokeNumsException() {
        super("Invoke nums is more than the MAX_INVOKE_NUMS !");
    }
}
