package com.sinoufc.jarinvoke.async;
/**
 *  用于操作完成之后的回调
 */
public interface UFCCallback {
	/**
     * 操作完成后的回调接口
     * @param result    包含支付或者查询结果信息
     */
    void done(UFCResult result);
}
