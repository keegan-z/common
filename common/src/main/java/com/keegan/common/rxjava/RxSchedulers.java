package com.keegan.common.rxjava;


import android.os.Looper;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxSchedulers {


    /**
     * 执行IO操作
     *
     * @param observable Observable
     * @param callback   回调
     */
    public static <K> void executeIO(Observable<?> observable, String tag, final IHandlerFuc<K> iHandlerFuc, final IRequestCallBack callback) {
        observable
                .compose(RxSchedulers.preTransformer(tag, iHandlerFuc))
                .compose(RxSchedulers.IOSubscribeMainObserve())
                .subscribe(new BaseObserver() {

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(e);

                        Log.i("main Thread", "" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                    }

                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        Log.i("main Thread", "" + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                        callback.onSuccess(o);
                    }
                });
    }


    /**
     * 执行耗时计算操作
     *
     * @param callback 回调
     */
    public static <K> void executeComputation(String tag, IHandlerFuc<K> iHandlerFuc, final IRequestCallBack callback, Object... objects) {
        createObservable(tag, iHandlerFuc, objects)
                .compose(RxSchedulers.computationSubscribeMainObserve())
                .subscribe(new BaseObserver() {

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null) {
                            callback.onFail(e);
                        }

                    }

                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        if (callback != null) {
                            callback.onSuccess(o);
                        }

                    }
                });
    }

    /**
     * 创建一个Observable
     *
     * @param iHandlerFuc call回调
     * @return Observable
     */
    public static <T> Observable<T> createObservable(final String tag, final IHandlerFuc<T> iHandlerFuc, final Object... t) {

        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter observableEmitter) throws Exception {
                if (iHandlerFuc != null) {
//                    Observable.just(iHandlerFuc.doInBackground(tag, t));
                    observableEmitter.onNext(iHandlerFuc.doInBackground(tag, t));
//                    return;
                }
                observableEmitter.onComplete();
            }

        });
    }

    /**
     * 处理线程调度的变换 用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；
     * 对于普通的计算任务，请使用Schedulers.computation()
     *
     * @return Observable.Transformer
     */
    public static ObservableTransformer IOSubscribeMainObserve() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 处理线程调度的变换 用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；
     * 默认线程数等于处理器的数量
     *
     * @return Observable.Transformer
     */
    public static ObservableTransformer computationSubscribeMainObserve() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 结果预处理及异常处理
     *
     * @return Observable.Transformer
     */
    public static <T, K> ObservableTransformer preTransformer(final String tag, final IHandlerFuc<K> iHandlFuc) {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable.flatMap(new HandleFuc<T>(tag, iHandlFuc))
                        .onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }


    private static class HandleFuc<T> implements Function<T, Observable> {

        private IHandlerFuc mIHandlFuc;
        private String tag;

        public HandleFuc(String tag, IHandlerFuc fuc) {
            this.tag = tag;
            this.mIHandlFuc = fuc;
        }

        @Override
        public Observable apply(T t) throws Exception {
            if (mIHandlFuc != null) {
                return Observable.just(mIHandlFuc.doInBackground(tag, t));
            }
            return Observable.just(t);
        }
    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {

        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(throwable);
        }
    }

    public interface IHandlerFuc<K> {
        /**
         * 调用线程处理相关操作，一般是在子线程做数据解析等操作，防止在主线程耗时太久导致ANR
         *
         * @param <T> 数据类型
         * @param tag 标识位
         * @param t   操作对象
         * @return 处理后需要返回的新得对象
         */
        <T> K doInBackground(String tag, T t);
    }

    static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }

}
