package taipt4.kotlin.databasewithroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import taipt4.kotlin.databasewithroom.database.UserRepositoryImp


class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<*>

    // Database
    private var compositeDisposable: CompositeDisposable? = null
    private var userRepository: UserRepositoryImp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}