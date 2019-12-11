package com.lujieni.hystrix;

import com.lujieni.hystrix.service.ObservableCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;
import rx.Observer;
import rx.observables.BlockingObservable;

import java.util.Iterator;

/**
 * @Auther ljn
 * @Date 2019/12/10
 * 测试ObservableCommand类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObservableCommandTest {

    /*
       observe不管有没有订阅者都会执行run或者construct中的代码
     */
    @Test
    public void obs(){
        ObservableCommand observableCommand = new ObservableCommand("allen");
        Observable<String> observe = observableCommand.observe();//不阻塞
        System.out.println("obs:"+Thread.currentThread().getName());

        observe.subscribe(new Observer<String>() {
            /*
               回调方法是由别的线程执行的!!!
             */
            @Override
            public void onCompleted() {
                System.out.println("obs completed:"+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("obs error:"+Thread.currentThread().getName());
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("obs onNext: " + v+":"+Thread.currentThread().getName());
            }
        });
    }

    /*
        没有subscribe也会让别的线程执行逻辑
     */
    @Test
    public void obsWithoutSub(){
        ObservableCommand observableCommand = new ObservableCommand("allen");
        Observable<String> observe = observableCommand.observe();
    }

    @Test
    public void tobs(){
        ObservableCommand observableCommand = new ObservableCommand("allen");
        Observable<String> observe = observableCommand.toObservable();

        observe.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("obs completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("obs error");
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("obs onNext: " + v);
            }

        });
    }

    /*
       toObservable没有subscribie的话不会执行业务逻辑
     */
    @Test
    public void tobsWithoutSub(){
        ObservableCommand observableCommand = new ObservableCommand("allen");
        Observable<String> observe = observableCommand.toObservable();
    }

    @Test
    public void blocking() {
        ObservableCommand observableCommand = new ObservableCommand("allen");
        Observable<String> observe = observableCommand.observe();
        BlockingObservable<String> stringBlockingObservable = observe.toBlocking();
        System.out.println("before:"+Thread.currentThread().getName());//before:main
        Iterator<String> iterator = stringBlockingObservable.getIterator();
        while(iterator.hasNext()) {
            /* Hello:main */
            /* allen !:main */
            /* 我认为这里会阻塞,因为是main线程获取数据,而不是别人的回调 */
            System.out.println(iterator.next()+":"+Thread.currentThread().getName());
        }
    }



}
