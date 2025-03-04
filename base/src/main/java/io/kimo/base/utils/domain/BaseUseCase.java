package io.kimo.base.utils.domain;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

public abstract class BaseUseCase<T> implements UseCase<T>, Runnable {

    public Callback<T> mCallback;
    protected String mErrorReason = "Something wrong happened";
    protected EventHandler mMainThread = new EventHandler(EventRunner.getMainEventRunner());

    public BaseUseCase(Callback<T> mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void run() {
        try {
            final T result = perform();
            mMainThread.postTask(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(result);
                }
            });
        } catch (Exception e) {
            mErrorReason = e.getMessage();
            onError();
        }
    }

    @Override
    public void onError() {
        mMainThread.postTask(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(mErrorReason);
            }
        });
    }
}
