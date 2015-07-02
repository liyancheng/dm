package com.tbc.framework.dm.common;

/**
 * @Package: com.tbc.framework.dm.common
 * @ClassName: Constant
 * @Description: 常量设置
 * @Author: liyancheng
 * @CreateDate: 7/1/2015 10:10 AM
 */
public class DMConstant {

    /**
     * 状态设置
     */
    public interface StatusConstant {

        /**
         * 执行中
         */
        public static final String DATA_MOVE_EXECUTING = "executing";

        /**
         * 未执行
         */
        public static final String DATA_MOVE_NOT_EXECUTING = "notExecuting";

        /**
         * 执行失败
         */
        public static final String DATA_MOVE_FAILED = "failed";

        /**
         * 执行成功
         */
        public static final String DATA_MOVE_SUCCESS = "success";

    }
}
