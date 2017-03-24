
package com.ziyuan.perspective;

import com.ziyuan.perspective.invokes.Branch;

/**
 * LocalManager threadLocal管理器，单例
 * 考虑废弃掉,统一放到storage处理
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public final class LocalManager {

    private ThreadLocal<Branch> LOCAL_BRANCH = new ThreadLocal<Branch>();

    public Branch getOwnBranch() {
        return LOCAL_BRANCH.get();
    }

    public void removeBranch() {
        LOCAL_BRANCH.remove();
    }

    public void addBranch(Branch branch) {
        LOCAL_BRANCH.set(branch);
    }

    /**
     * 单例
     */
    private LocalManager() {
    }

    private static final LocalManager localTraceManager = new LocalManager();

    public static LocalManager getManager() {
        return localTraceManager;
    }
}
