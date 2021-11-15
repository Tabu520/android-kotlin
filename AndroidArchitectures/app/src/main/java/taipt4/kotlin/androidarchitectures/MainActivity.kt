package taipt4.kotlin.androidarchitectures

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import taipt4.kotlin.androidarchitectures.adapter.CountriesAdapter
import taipt4.kotlin.androidarchitectures.model.Country
import taipt4.kotlin.androidarchitectures.networking.CountriesApi
import taipt4.kotlin.androidarchitectures.networking.CountriesService

class MainActivity : AppCompatActivity() {

    private var apiService: CountriesApi? = null
    private val countriesAdapter = CountriesAdapter(arrayListOf())
    private var countries: List<Country>? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var listCountry: RecyclerView
    private lateinit var searchField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progress)
        listCountry = findViewById(R.id.listView)
        searchField = findViewById(R.id.searchField)

        apiService = CountriesService.create()
        listCountry.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }
        countriesAdapter.setOnItemClickListener(object: CountriesAdapter.OnItemClickListener {
            override fun onItemClick(country: Country) {
                Toast.makeText(this@MainActivity, "Country ${country.name}, capital is ${country.capital} clicked", Toast.LENGTH_SHORT).show()
            }

        })
        searchField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    val filterCountries = countries?.filter { country ->
                        country.name.contains(s.toString(), true)
                    }
                    filterCountries?.let { countriesAdapter.updateCountries(it) }
                } else {
                    countries?.let { countriesAdapter.updateCountries(it) }
                }
            }

        })
        onFetchCountries()
    }

    private fun onFetchCountries() {
        listCountry.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        searchField.isEnabled = false

        apiService?.let {
            it.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    progressBar.visibility = View.GONE
                    listCountry.visibility = View.VISIBLE
                    searchField.isEnabled = true
                    countries = result
                    countriesAdapter.updateCountries(result)
                }, {
                    onError()
                })
        }
    }

    private fun onError() {
        listCountry.visibility = View.GONE
        progressBar.visibility = View.GONE
        searchField.isEnabled = false
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}