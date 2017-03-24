
package com.ziyuan.perspective.invokes;

/**
 * AbstractInvoke invoke抽象类
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public abstract class AbstractInvoke implements Invoke {

    /**
     * 这个invoke的name(推荐以这次调用的方法名为name)
     */
    private final String name;

    /**
     * 一个invoke的持续时间
     */
    private long duration;

    /**
     * 这是个全局Id,标识这个invoke属于哪个trace
     */
    private final String traceId;

    /**
     * 参数
     */
    private Object[] args;

    /**
     * 状态
     */
    private InvokeState state;

    /**
     * 错误
     */
    private Throwable error;

    /**
     * 开始时间
     */
    private long startTime;

    protected AbstractInvoke(String name, String traceId) {
        this.name = name;
        this.traceId = traceId;
        this.state = InvokeState.TRACING;
        this.startTime = System.currentTimeMillis();
    }

    public abstract String format();

    public boolean finished() {
        return this.state.getValue() >= InvokeState.OVER.getValue();
    }

    public boolean isSuccess() {
        return this.state == InvokeState.OVER;
    }

    public String getName() {
        return this.name;
    }

    public long getDuration() {
        return this.duration;
    }

    public InvokeState getState() {
        return this.state;
    }

    public void setState(InvokeState state) {
        this.state = state;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return this.error;
    }

    public void setArgs(Object... obj) {
        this.args = new Object[obj.length];
        for (int i = 0; i < args.length; i++) {
            this.args[i] = obj[i];
        }
    }

    public Object[] getArgs() {
        return this.args;
    }

    public String getTraceId() {
        return traceId;
    }

    /**
     * 这里设置为受保护的，不允许外部更改持续时间
     *
     * @param duration 持续时间
     */
    protected void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 获取当前invoke的开始时间
     *
     * @return 开始时间
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 是否超时
     *
     * @return 是否超时
     */
    public boolean isTimeOut() {
        return this.getState() == InvokeState.TIMEOUT;
    }

    public int compareTo(Invoke o) {
        if (this.getDuration() > o.getDuration()) {
            return 1;
        } else if (this.getDuration() < o.getDuration()) {
            return -1;
        } else {
            return 0;
        }
    }
}
