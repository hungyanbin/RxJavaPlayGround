package yanbin.com.rxjavaplayground

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var message: String = ""
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStart.setOnClickListener { startObservable() }
        btnStop.setOnClickListener { disposable.dispose() }
    }

    private fun startObservable() {
        val observable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .doOnNext {
                    message += "message $it \n"
                    renderMessage()
                }
                .flatMap { flatObservable(it) }
        disposable.add(observable.subscribe())
    }

    private fun flatObservable(second: Long): Observable<Long> {
        return Observable.just(second)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    message += "flat message $it \n"
                    renderMessage()
                }
    }

    private fun renderMessage() {
        textView.post {
            textView.text = message
        }
    }


}
