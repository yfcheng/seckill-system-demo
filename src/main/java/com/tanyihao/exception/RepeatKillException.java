package com.tanyihao.exception;

/**
 * 重复秒杀异常，运行期的异常
 * Created by tanhao on 16-12-28.
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
