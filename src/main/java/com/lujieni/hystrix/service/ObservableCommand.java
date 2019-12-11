package com.lujieni.hystrix.service;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @Auther ljn
 * @Date 2019/12/9
 * HystrixObservableCommand的逻辑是在construct中，
 * 失败函数也和HystrixCommand的getFailBack不同，而
 * 是使用的resumeWithFallback
 */
public class ObservableCommand extends HystrixObservableCommand<String> {

    private final String name;

    public ObservableCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("obs")).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(60000)));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        System.out.println("construct:"+Thread.currentThread().getName());//同步执行,打印construct
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        System.out.println("call:"+Thread.currentThread().getName());
                        observer.onNext("Hello");
                        observer.onNext(name + " !");
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        } ).subscribeOn(Schedulers.io());
    }

    public Observable<String> resumeWithFallback(){
        return null;
    }


}
