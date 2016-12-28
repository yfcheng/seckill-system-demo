package com.tanyihao.exception;

/**
 * 秒杀关闭异常
 * Created by tanhao on 16-12-28.
 */
public class SeckillClosedException extends SeckillException{
    public SeckillClosedException(String message) {
        super(message);
    }

    public SeckillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
