
package com.ziyuan.perspective.Exception;

/**
 * SymbolIdEmptyException TraceId或者branchId为空抛出的异常
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public class SymbolIdEmptyException extends Exception {

    public SymbolIdEmptyException() {
        super("TraceId or BranchId is null/blank !");
    }
}
